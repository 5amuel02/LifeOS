package com.example.lifeos.data.habit

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class HabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val completionDao: HabitCompletionDao,
) {
    fun getAllHabits(): Flow<List<HabitEntity>> = habitDao.getAllHabits()

    fun getAllCompletions(): Flow<List<HabitCompletionEntity>> = completionDao.getAllCompletions()

    suspend fun getAllHabitsOnce(): List<HabitEntity> = habitDao.getAllHabitsOnce()

    /** Inserts a new habit and returns its generated id. */
    suspend fun addHabit(name: String, reminderMinuteOfDay: Int?): Long {
        return habitDao.insert(
            HabitEntity(
                name = name,
                createdAt = System.currentTimeMillis(),
                reminderMinuteOfDay = reminderMinuteOfDay,
            )
        )
    }

    suspend fun updateReminder(id: Long, minuteOfDay: Int?) = habitDao.updateReminder(id, minuteOfDay)

    suspend fun deleteHabit(id: Long) = habitDao.deleteById(id)

    suspend fun toggleToday(habitId: Long, isCurrentlyDoneToday: Boolean) {
        val today = LocalDate.now().toEpochDay()
        if (isCurrentlyDoneToday) {
            completionDao.deleteForDay(habitId, today)
        } else {
            completionDao.insert(HabitCompletionEntity(habitId = habitId, dateEpochDay = today))
        }
    }
}

fun calculateStreak(completedDays: Set<Long>): Int {
    val today = LocalDate.now().toEpochDay()
    val anchor = if (today in completedDays) today else today - 1
    var streak = 0
    var day = anchor
    while (day in completedDays) {
        streak++
        day--
    }
    return streak
}
