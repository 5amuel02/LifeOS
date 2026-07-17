package com.example.lifeos.data.pomodoro

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroSessionDao {
    @Query("SELECT * FROM pomodoro_sessions")
    fun getAllSessions(): Flow<List<PomodoroSessionEntity>>

    @Insert
    suspend fun insert(session: PomodoroSessionEntity)
}
