package com.example.lifeos.data.pomodoro

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class PomodoroRepository(private val dao: PomodoroSessionDao) {
    fun getAllSessions(): Flow<List<PomodoroSessionEntity>> = dao.getAllSessions()

    suspend fun recordCompletedSession() {
        dao.insert(
            PomodoroSessionEntity(
                completedAt = System.currentTimeMillis(),
                dateEpochDay = LocalDate.now().toEpochDay()
            )
        )
    }
}
