package com.example.lifeos.ui.screens.targethidup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.LifeOSDatabase
import com.example.lifeos.data.targethidup.TargetHidupEntity
import com.example.lifeos.data.targethidup.TargetHidupRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TargetHidupViewModel(application: Application) : AndroidViewModel(application) {
    private val database = LifeOSDatabase.getInstance(application)
    private val repository = TargetHidupRepository(database.targetHidupDao())

    val goals: StateFlow<List<TargetHidupEntity>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addGoal(title: String, targetDateEpochDay: Long?) {
        if (title.isBlank()) return
        viewModelScope.launch { repository.addGoal(title.trim(), targetDateEpochDay) }
    }

    fun updateProgress(id: Long, progressPercent: Int) {
        viewModelScope.launch { repository.updateProgress(id, progressPercent) }
    }

    fun deleteGoal(id: Long) {
        viewModelScope.launch { repository.deleteGoal(id) }
    }
}
