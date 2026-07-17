package com.example.lifeos.data.targethidup

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TargetHidupDao {
    @Query("SELECT * FROM target_hidup ORDER BY (targetDateEpochDay IS NULL) ASC, targetDateEpochDay ASC, createdAt ASC")
    fun getAll(): Flow<List<TargetHidupEntity>>

    @Insert
    suspend fun insert(item: TargetHidupEntity): Long

    @Query("UPDATE target_hidup SET progressPercent = :progressPercent WHERE id = :id")
    suspend fun updateProgress(id: Long, progressPercent: Int)

    @Query("DELETE FROM target_hidup WHERE id = :id")
    suspend fun deleteById(id: Long)
}
