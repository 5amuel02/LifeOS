package com.example.lifeos.data.notes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

data class ChecklistSummary(
    val noteId: Long,
    val total: Int,
    val checkedCount: Int,
)

@Dao
interface ChecklistItemDao {
    @Query("SELECT * FROM checklist_items WHERE noteId = :noteId ORDER BY position ASC")
    fun getItemsForNote(noteId: Long): Flow<List<ChecklistItemEntity>>

    @Query("SELECT * FROM checklist_items WHERE noteId = :noteId ORDER BY position ASC")
    suspend fun getItemsForNoteOnce(noteId: Long): List<ChecklistItemEntity>

    @Query(
        "SELECT noteId, COUNT(*) AS total, SUM(CASE WHEN isChecked THEN 1 ELSE 0 END) AS checkedCount " +
            "FROM checklist_items GROUP BY noteId"
    )
    fun getChecklistSummaries(): Flow<List<ChecklistSummary>>

    @Insert
    suspend fun insert(item: ChecklistItemEntity): Long

    @Update
    suspend fun update(item: ChecklistItemEntity)

    @Delete
    suspend fun delete(item: ChecklistItemEntity)

    @Query("DELETE FROM checklist_items WHERE id = :id")
    suspend fun deleteById(id: Long)
}
