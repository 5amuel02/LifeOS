package com.example.lifeos.ui.screens.aiassistant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.data.chat.ChatMessageEntity
import com.example.lifeos.ui.screens.common.PlaceholderScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: AiAssistantViewModel = hiltViewModel()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    var input by rememberSaveable { mutableStateOf("") }
    var showClearDialog by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.aiAssistant.aiAssistantTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.shared.back)
                    }
                },
                actions = {
                    if (messages.isNotEmpty()) {
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(Icons.Filled.DeleteSweep, contentDescription = strings.aiAssistant.aiAssistantClearContentDesc)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (messages.isEmpty()) {
                Box(modifier = Modifier.weight(1f)) {
                    PlaceholderScreen(
                        title = strings.aiAssistant.aiAssistantTitle,
                        subtitle = strings.aiAssistant.aiAssistantEmptySubtitle,
                        icon = Icons.Filled.SmartToy
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(messages, key = { it.id }) { message ->
                        ChatBubble(message)
                    }
                }
            }

            if (viewModel.isSending) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.padding(2.dp), strokeWidth = 2.dp)
                    Text(
                        text = strings.aiAssistant.aiAssistantThinkingLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            val currentError = viewModel.error
            if (currentError != null) {
                ChatErrorCard(
                    error = currentError,
                    strings = strings,
                    onDismiss = viewModel::dismissError
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    placeholder = { Text(strings.aiAssistant.aiAssistantInputPlaceholder) },
                    modifier = Modifier.weight(1f),
                    enabled = !viewModel.isSending
                )
                IconButton(
                    onClick = {
                        viewModel.sendMessage(input)
                        input = ""
                    },
                    enabled = !viewModel.isSending && input.isNotBlank()
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = strings.aiAssistant.aiAssistantSendContentDesc)
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(strings.aiAssistant.aiAssistantClearDialogTitle) },
            text = { Text(strings.aiAssistant.aiAssistantClearDialogText) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearChat()
                    showClearDialog = false
                }) { Text(strings.shared.delete) }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text(strings.shared.cancel) }
            }
        )
    }
}

@Composable
private fun ChatBubble(message: ChatMessageEntity) {
    val isUser = message.role == ChatMessageEntity.ROLE_USER
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            )
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isUser) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
private fun ChatErrorCard(
    error: ChatError,
    strings: LifeOSStrings,
    onDismiss: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = when (error) {
                    ChatError.RateLimited -> strings.aiAssistant.aiAssistantRateLimitMessage
                    is ChatError.ApiFailure -> strings.aiAssistant.aiAssistantErrorPrefixTemplate.format(error.message)
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onDismiss) {
                Text(strings.shared.cancel)
            }
        }
    }
}
