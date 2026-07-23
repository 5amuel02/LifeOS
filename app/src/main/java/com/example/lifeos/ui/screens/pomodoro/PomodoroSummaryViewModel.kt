package com.example.lifeos.ui.screens.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.pomodoro.PomodoroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

@HiltViewModel
class PomodoroSummaryViewModel @Inject constructor(
    private val repository: PomodoroRepository,
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    val sessionTimesForDate: StateFlow<List<Long>> = combine(
        repository.getAllSessions(),
        _selectedDate
    ) { sessions, date ->
        val epochDay = date.toEpochDay()
        sessions.filter { it.dateEpochDay == epochDay }
            .map { it.completedAt }
            .sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }
}
