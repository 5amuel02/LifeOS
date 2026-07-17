package com.example.lifeos.data.notes

import kotlinx.coroutines.flow.Flow

class NoteRepository(private val dao: NoteDao) {
    fun getAllNotes(): Flow<List<NoteEntity>> = dao.getAllNotes()

    suspend fun getNoteById(id: Long): NoteEntity? = dao.getNoteById(id)

    suspend fun saveNote(id: Long?, title: String, content: String, color: String, noteType: String): Long {
        val now = System.currentTimeMillis()
        return if (id == null) {
            dao.insert(
                NoteEntity(
                    title = title,
                    content = content,
                    createdAt = now,
                    updatedAt = now,
                    color = color,
                    noteType = noteType
                )
            )
        } else {
            val existing = dao.getNoteById(id)
            if (existing != null) {
                dao.update(existing.copy(title = title, content = content, updatedAt = now, color = color, noteType = noteType))
            }
            id
        }
    }

    suspend fun deleteNote(id: Long) = dao.deleteById(id)

    suspend fun togglePin(id: Long, isCurrentlyPinned: Boolean) = dao.setPinned(id, !isCurrentlyPinned)
}
