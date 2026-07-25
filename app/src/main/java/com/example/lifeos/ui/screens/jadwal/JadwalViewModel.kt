package com.example.lifeos.ui.screens.jadwal

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.jadwal.JadwalEntity
import com.example.lifeos.data.jadwal.JadwalRepository
import com.example.lifeos.notifications.JadwalReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class JadwalViewModel @Inject constructor(
    private val repository: JadwalRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    val items: StateFlow<List<JadwalEntity>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addItem(title: String, date: LocalDate, minuteOfDay: Int?) {
        if (title.isBlank()) return
        val trimmedTitle = title.trim()
        viewModelScope.launch {
            val id = repository.addItem(trimmedTitle, date.toEpochDay(), minuteOfDay)
            if (minuteOfDay != null) {
                JadwalReminderScheduler.schedule(context, id, trimmedTitle, date.toEpochDay(), minuteOfDay)
            }
        }
    }

    fun toggleCompleted(item: JadwalEntity) {
        val nowCompleted = !item.isCompleted
        viewModelScope.launch {
            repository.setCompleted(item.id, nowCompleted)
            if (nowCompleted) {
                JadwalReminderScheduler.cancel(context, item.id)
            } else {
                item.minuteOfDay?.let { minute ->
                    JadwalReminderScheduler.schedule(context, item.id, item.title, item.dateEpochDay, minute)
                }
            }
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch {
            JadwalReminderScheduler.cancel(context, id)
            repository.deleteItem(id)
        }
    }
}
