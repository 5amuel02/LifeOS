package com.example.lifeos.ui.screens.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.habit.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

data class HabitSummaryItem(
    val id: Long,
    val name: String,
    val isDone: Boolean,
)

@HiltViewModel
class HabitSummaryViewModel @Inject constructor(
    private val repository: HabitRepository,
) : ViewModel() {

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
