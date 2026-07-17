package com.example.lifeos.ui.screens.habit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.LifeOSDatabase
import com.example.lifeos.data.habit.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class HabitSummaryItem(
    val id: Long,
    val name: String,
    val isDone: Boolean,
)

class HabitSummaryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = LifeOSDatabase.getInstance(application)
    private val repository = HabitRepository(database.habitDao(), database.habitCompletionDao())

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    val summaryItems: StateFlow<List<HabitSummaryItem>> = combine(
        repository.getAllHabits(),
        repository.getAllCompletions(),
        _selectedDate
    ) { habits, completions, date ->
        val epochDay = date.toEpochDay()
        habits.map { habit ->
            HabitSummaryItem(
                id = habit.id,
                name = habit.name,
                isDone = completions.any { it.habitId == habit.id && it.dateEpochDay == epochDay }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }
}
