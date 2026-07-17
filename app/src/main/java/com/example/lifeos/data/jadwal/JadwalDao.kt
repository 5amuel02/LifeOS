package com.example.lifeos.data.jadwal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JadwalDao {
    @Query("SELECT * FROM jadwal ORDER BY dateEpochDay ASC, minuteOfDay ASC")
    fun getAll(): Flow<List<JadwalEntity>>

    @Query("SELECT * FROM jadwal")
    suspend fun getAllOnce(): List<JadwalEntity>

    @Query("SELECT * FROM jadwal WHERE id = :id LIMIT 1")
    suspend fun getByIdOnce(id: Long): JadwalEntity?

    @Insert
    suspend fun insert(item: JadwalEntity): Long

    @Query("UPDATE jadwal SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompleted(id: Long, isCompleted: Boolean)

    @Query("DELETE FROM jadwal WHERE id = :id")
    suspend fun deleteById(id: Long)
}
