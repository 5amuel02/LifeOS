package com.example.lifeos.data.habit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt ASC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits")
    suspend fun getAllHabitsOnce(): List<HabitEntity>

    @Insert
    suspend fun insert(habit: HabitEntity): Long

    @Query("UPDATE habits SET reminderMinuteOfDay = :minuteOfDay WHERE id = :id")
    suspend fun updateReminder(id: Long, minuteOfDay: Int?)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteById(id: Long)
}
