package com.example.lifeos.ui.screens.pomodoro

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.LifeOSDatabase
import com.example.lifeos.data.pomodoro.PomodoroRepository
import com.example.lifeos.data.settings.AppSettings
import com.example.lifeos.data.settings.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val SESSIONS_BEFORE_LONG_BREAK = 4
private const val PHASE_END_VIBRATION_MS = 400L

enum class PomodoroPhase { FOCUS, SHORT_BREAK, LONG_BREAK }

class PomodoroViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PomodoroRepository(LifeOSDatabase.getInstance(application).pomodoroSessionDao())
    private val settingsRepository = SettingsRepository(application)
    private var timerJob: Job? = null
    private var currentSettings = AppSettings()

    var phase by mutableStateOf(PomodoroPhase.FOCUS)
        private set
    var secondsRemaining by mutableIntStateOf(durationForPhase(PomodoroPhase.FOCUS, currentSettings))
        private set
    var totalSeconds by mutableIntStateOf(durationForPhase(PomodoroPhase.FOCUS, currentSettings))
        private set
    var isRunning by mutableStateOf(false)
        private set
    var focusSessionsInCycle by mutableIntStateOf(0)
        private set

    val sessionsToday: StateFlow<Int> = repository.getAllSessions()
        .map { sessions ->
            val today = LocalDate.now().toEpochDay()
            sessions.count { it.dateEpochDay == today }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                currentSettings = settings
                if (!isRunning) {
                    val duration = durationForPhase(phase, settings)
                    secondsRemaining = duration
                    totalSeconds = duration
                }
            }
        }
    }

    fun start() {
        if (isRunning) return
        isRunning = true
        timerJob = viewModelScope.launch {
            while (secondsRemaining > 0) {
                delay(1_000)
                secondsRemaining -= 1
            }
            onPhaseFinished()
        }
    }

    fun pause() {
        isRunning = false
        timerJob?.cancel()
    }

    fun reset() {
        pause()
        val duration = durationForPhase(phase, currentSettings)
        secondsRemaining = duration
        totalSeconds = duration
    }

    fun skip() {
        pause()
        advancePhase()
    }

    private fun onPhaseFinished() {
        isRunning = false
        vibrate()
        if (phase == PomodoroPhase.FOCUS) {
            viewModelScope.launch { repository.recordCompletedSession() }
        }
        advancePhase()
    }

    private fun advancePhase() {
        phase = when (phase) {
            PomodoroPhase.FOCUS -> {
                focusSessionsInCycle += 1
                if (focusSessionsInCycle >= SESSIONS_BEFORE_LONG_BREAK) {
                    focusSessionsInCycle = 0
                    PomodoroPhase.LONG_BREAK
                } else {
                    PomodoroPhase.SHORT_BREAK
                }
            }
            PomodoroPhase.SHORT_BREAK, PomodoroPhase.LONG_BREAK -> PomodoroPhase.FOCUS
        }
        val duration = durationForPhase(phase, currentSettings)
        secondsRemaining = duration
        totalSeconds = duration
    }

    private fun vibrate() {
        val context = getApplication<Application>()
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(PHASE_END_VIBRATION_MS, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(PHASE_END_VIBRATION_MS)
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}

private fun durationForPhase(phase: PomodoroPhase, settings: AppSettings): Int = when (phase) {
    PomodoroPhase.FOCUS -> settings.focusMinutes * 60
    PomodoroPhase.SHORT_BREAK -> settings.shortBreakMinutes * 60
    PomodoroPhase.LONG_BREAK -> settings.longBreakMinutes * 60
}
