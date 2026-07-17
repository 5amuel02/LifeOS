package com.example.lifeos.data.chat

import kotlinx.coroutines.flow.Flow

class ChatRepository(private val dao: ChatMessageDao) {
    fun getAll(): Flow<List<ChatMessageEntity>> = dao.getAll()

    suspend fun getAllOnce(): List<ChatMessageEntity> = dao.getAllOnce()

    suspend fun addMessage(role: String, content: String): Long {
        return dao.insert(
            ChatMessageEntity(
                role = role,
                content = content,
                createdAt = System.currentTimeMillis(),
            )
        )
    }

    suspend fun clearAll() = dao.deleteAll()
}
