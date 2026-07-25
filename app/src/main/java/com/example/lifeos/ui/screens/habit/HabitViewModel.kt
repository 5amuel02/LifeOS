package com.example.lifeos.ui.screens.habit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.habit.HabitRepository
import com.example.lifeos.data.habit.calculateStreak
import com.example.lifeos.notifications.HabitReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HabitUi(
    val id: Long,
    val name: String,
    val isDoneToday: Boolean,
    val streak: Int,
    val reminderMinuteOfDay: Int?,
)

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repository: HabitRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    val habits: StateFlow<List<HabitUi>> = combine(
        repository.getAllHabits(),
        repository.getAllCompletions()
    ) { habits, completions ->
        val completedDaysByHabit = completions.groupBy(keySelector = { it.habitId }) { it.dateEpochDay }
        val today = LocalDate.now().toEpochDay()
        habits.map { habit ->
            val completedDays = completedDaysByHabit[habit.id]?.toSet() ?: emptySet()
            HabitUi(
                id = habit.id,
                name = habit.name,
                isDoneToday = today in completedDays,
                streak = calculateStreak(completedDays),
                reminderMinuteOfDay = habit.reminderMinuteOfDay
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addHabit(name: String, reminderMinuteOfDay: Int?) {
        if (name.isBlank()) return
        val trimmedName = name.trim()
        viewModelScope.launch {
            val id = repository.addHabit(trimmedName, reminderMinuteOfDay)
            if (reminderMinuteOfDay != null) {
                HabitReminderScheduler.schedule(context, id, trimmedName, reminderMinuteOfDay)
            }
        }
    }

    fun updateReminder(habit: HabitUi, reminderMinuteOfDay: Int?) {
        viewModelScope.launch {
            repository.updateReminder(habit.id, reminderMinuteOfDay)
            if (reminderMinuteOfDay != null) {
                HabitReminderScheduler.schedule(context, habit.id, habit.name, reminderMinuteOfDay)
            } else {
                HabitReminderScheduler.cancel(context, habit.id)
            }
        }
    }

    fun deleteHabit(id: Long) {
        viewModelScope.launch {
            HabitReminderScheduler.cancel(context, id)
            repository.deleteHabit(id)
        }
    }

    fun toggleToday(habit: HabitUi) {
        viewModelScope.launch { repository.toggleToday(habit.id, habit.isDoneToday) }
    }
}
