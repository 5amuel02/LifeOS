package com.example.lifeos.ui.screens.beranda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.habit.HabitRepository
import com.example.lifeos.data.notes.NoteEntity
import com.example.lifeos.data.notes.NoteRepository
import com.example.lifeos.data.pomodoro.PomodoroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

data class HabitSummary(val doneToday: Int, val total: Int) {
    val progress: Float get() = if (total == 0) 0f else doneToday.toFloat() / total.toFloat()
}

@HiltViewModel
class BerandaViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val habitRepository: HabitRepository,
    private val pomodoroRepository: PomodoroRepository,
) : ViewModel() {

    val notes: StateFlow<List<NoteEntity>> = noteRepository.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val habitSummary: StateFlow<HabitSummary> = combine(
        habitRepository.getAllHabits(),
        habitRepository.getAllCompletions()
    ) { habits, completions ->
        val today = LocalDate.now().toEpochDay()
        val doneToday = habits.count { habit ->
            completions.any { it.habitId == habit.id && it.dateEpochDay == today }
        }
        HabitSummary(doneToday = doneToday, total = habits.size)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HabitSummary(0, 0))

    val pomodoroSessionsToday: StateFlow<Int> = pomodoroRepository.getAllSessions()
        .map { sessions ->
            val today = LocalDate.now().toEpochDay()
            sessions.count { it.dateEpochDay == today }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
}
