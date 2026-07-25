package com.example.lifeos.ui.screens.statistik

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.habit.HabitRepository
import com.example.lifeos.data.habit.calculateStreak
import com.example.lifeos.data.notes.NoteDao
import com.example.lifeos.data.pomodoro.PomodoroRepository
import com.example.lifeos.data.targethidup.TargetHidupDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

data class DayValue(val dateEpochDay: Long, val value: Float)

data class StatistikUiState(
    val totalNotes: Int = 0,
    val totalPomodoroSessions: Int = 0,
    val activeHabits: Int = 0,
    val longestStreak: Int = 0,
    val goalsAchieved: Int = 0,
    val habitsCompletedToday: Int = 0,
    val pomodoroLast7Days: List<DayValue> = emptyList(),
    val habitRateLast7Days: List<DayValue> = emptyList(),
)

@HiltViewModel
class StatistikViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val pomodoroRepository: PomodoroRepository,
    private val noteDao: NoteDao,
    private val targetHidupDao: TargetHidupDao,
) : ViewModel() {

    val uiState: StateFlow<StatistikUiState> = combine(
        habitRepository.getAllHabits(),
        habitRepository.getAllCompletions(),
        pomodoroRepository.getAllSessions(),
        noteDao.getAllNotes(),
        targetHidupDao.getAll(),
    ) { habits, completions, sessions, notes, goals ->
        val today = LocalDate.now().toEpochDay()
        val last7Days = (6 downTo 0).map { today - it }

        val completionsByDay = completions.groupingBy { it.dateEpochDay }.eachCount()
        val sessionsByDay = sessions.groupingBy { it.dateEpochDay }.eachCount()

        val longestStreak = if (habits.isEmpty()) {
            0
        } else {
            val completedDaysByHabit = completions.groupBy(keySelector = { it.habitId }) { it.dateEpochDay }
            habits.maxOf { habit -> calculateStreak(completedDaysByHabit[habit.id]?.toSet() ?: emptySet()) }
        }

        StatistikUiState(
            totalNotes = notes.size,
            totalPomodoroSessions = sessions.size,
            activeHabits = habits.size,
            longestStreak = longestStreak,
            goalsAchieved = goals.count { it.progressPercent >= 100 },
            habitsCompletedToday = (completionsByDay[today] ?: 0).coerceAtMost(habits.size),
            pomodoroLast7Days = last7Days.map { day -> DayValue(day, (sessionsByDay[day] ?: 0).toFloat()) },
            // Uses today's habit count as the denominator for every day in the window instead of
            // reconstructing how many habits existed on each past day — simpler, and close enough
            // for a 7-day trend glance.
            habitRateLast7Days = last7Days.map { day ->
                val rate = if (habits.isEmpty()) 0f else (completionsByDay[day] ?: 0) * 100f / habits.size
                DayValue(day, rate)
            },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StatistikUiState())
}
