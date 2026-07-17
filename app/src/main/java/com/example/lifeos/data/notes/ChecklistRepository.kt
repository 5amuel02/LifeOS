package com.example.lifeos.data.notes

import kotlinx.coroutines.flow.Flow

class ChecklistRepository(private val dao: ChecklistItemDao) {
    fun getItemsForNote(noteId: Long): Flow<List<ChecklistItemEntity>> = dao.getItemsForNote(noteId)

    suspend fun getItemsForNoteOnce(noteId: Long): List<ChecklistItemEntity> = dao.getItemsForNoteOnce(noteId)

    fun getChecklistSummaries(): Flow<List<ChecklistSummary>> = dao.getChecklistSummaries()

    suspend fun syncItems(noteId: Long, items: List<ChecklistItemUi>) {
        val existing = dao.getItemsForNoteOnce(noteId)
        val keptIds = items.mapNotNull { it.id }.toSet()
        existing.filter { it.id !in keptIds }.forEach { dao.deleteById(it.id) }

        items.filter { it.text.isNotBlank() }.forEachIndexed { index, item ->
            if (item.id != null) {
                dao.update(
                    ChecklistItemEntity(
                        id = item.id,
                        noteId = noteId,
                        text = item.text,
                        isChecked = item.isChecked,
                        position = index
                    )
                )
            } else {
                dao.insert(
                    ChecklistItemEntity(
                        noteId = noteId,
                        text = item.text,
                        isChecked = item.isChecked,
                        position = index
                    )
                )
            }
        }
    }
}

data class ChecklistItemUi(
    val key: String,
    val id: Long? = null,
    val text: String = "",
    val isChecked: Boolean = false,
)
