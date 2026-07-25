package com.example.lifeos.ui.screens.aiassistant

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.chat.ChatMessageEntity
import com.example.lifeos.data.chat.ChatRepository
import com.example.lifeos.data.chat.DeviceIdProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.inject.Inject

sealed interface ChatError {
    data class ApiFailure(val message: String) : ChatError
    data object RateLimited : ChatError
}

// Replace with your deployed Cloudflare Worker URL after running `wrangler deploy` in /server.
private const val BACKEND_URL = "https://lifeos-ai-proxy.YOUR-SUBDOMAIN.workers.dev"

// Must match the APP_SHARED_SECRET secret set on the Worker (`wrangler secret put APP_SHARED_SECRET`).
private const val APP_SHARED_SECRET = "REPLACE_WITH_A_RANDOM_SECRET"

private const val MAX_HISTORY_MESSAGES = 20
private const val CONNECT_TIMEOUT_MS = 15_000
private const val READ_TIMEOUT_MS = 30_000

@HiltViewModel
class AiAssistantViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    @ApplicationContext context: Context,
) : ViewModel() {
    private val deviceId = DeviceIdProvider.getOrCreate(context)

    val messages: StateFlow<List<ChatMessageEntity>> = chatRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    var isSending by mutableStateOf(false)
        private set

    var error by mutableStateOf<ChatError?>(null)
        private set

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isBlank() || isSending) return

        viewModelScope.launch {
            error = null
            chatRepository.addMessage(ChatMessageEntity.ROLE_USER, trimmed)
            isSending = true
            try {
                val history = chatRepository.getAllOnce()
                val reply = withContext(Dispatchers.IO) { callBackend(history) }
                chatRepository.addMessage(ChatMessageEntity.ROLE_ASSISTANT, reply)
            } catch (e: RateLimitException) {
                error = ChatError.RateLimited
            } catch (e: Exception) {
                error = ChatError.ApiFailure(e.message ?: "Unknown error")
            } finally {
                isSending = false
            }
        }
    }

    fun dismissError() {
        error = null
    }

    fun clearChat() {
        viewModelScope.launch { chatRepository.clearAll() }
    }

    private class RateLimitException : Exception()

    private fun callBackend(history: List<ChatMessageEntity>): String {
        val messagesJson = JSONArray()
        history.takeLast(MAX_HISTORY_MESSAGES).forEach { message ->
            val role = if (message.role == ChatMessageEntity.ROLE_USER) "user" else "assistant"
            messagesJson.put(JSONObject().put("role", role).put("content", message.content))
        }
        val requestBody = JSONObject().put("messages", messagesJson).toString()

        val connection = URL(BACKEND_URL).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            connection.setRequestProperty("X-Device-Id", deviceId)
            connection.setRequestProperty("X-App-Secret", APP_SHARED_SECRET)
            connection.connectTimeout = CONNECT_TIMEOUT_MS
            connection.readTimeout = READ_TIMEOUT_MS

            OutputStreamWriter(connection.outputStream, StandardCharsets.UTF_8).use { it.write(requestBody) }

            val responseCode = connection.responseCode
            val stream = if (responseCode in 200..299) connection.inputStream else connection.errorStream
            val responseText = stream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }

            if (responseCode == 429) throw RateLimitException()
            if (responseCode !in 200..299) {
                val errorJson = runCatching { JSONObject(responseText) }.getOrNull()
                throw Exception(errorJson?.optString("error") ?: "HTTP $responseCode")
            }

            return JSONObject(responseText).optString("reply").ifBlank { "..." }
        } finally {
            connection.disconnect()
        }
    }
}
