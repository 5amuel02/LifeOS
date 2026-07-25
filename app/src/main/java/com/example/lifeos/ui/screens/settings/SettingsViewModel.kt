package com.example.lifeos.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.core.strings.AppLanguage
import com.example.lifeos.data.LifeOSDatabase
import com.example.lifeos.data.settings.AppSettings
import com.example.lifeos.data.settings.SettingsRepository
import com.example.lifeos.data.settings.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository,
    private val database: LifeOSDatabase,
) : ViewModel() {

    val settings: StateFlow<AppSettings> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppSettings())

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { repository.setThemeMode(mode) }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch { repository.setLanguage(language) }
    }

    fun setFocusMinutes(minutes: Int) {
        viewModelScope.launch { repository.setFocusMinutes(minutes) }
    }

    fun setShortBreakMinutes(minutes: Int) {
        viewModelScope.launch { repository.setShortBreakMinutes(minutes) }
    }

    fun setLongBreakMinutes(minutes: Int) {
        viewModelScope.launch { repository.setLongBreakMinutes(minutes) }
    }

    fun clearAllData(onDone: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { database.clearAllTables() }
            onDone()
        }
    }
}
