package com.example.lifeos.ui.screens.pomodoro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroScreen(onOpenHistory: () -> Unit = {}) {
    val strings = LocalStrings.current
    val viewModel: PomodoroViewModel = viewModel()
    val sessionsToday by viewModel.sessionsToday.collectAsStateWithLifecycle()
    var showHelp by rememberSaveable { mutableStateOf(false) }

    val progress = 1f - (viewModel.secondsRemaining.toFloat() / viewModel.totalSeconds.toFloat())
    val phaseColor = phaseColor(viewModel.phase)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.pomodoro.pomodoroTitle) },
                actions = {
                    IconButton(onClick = onOpenHistory) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = strings.pomodoro.pomodoroHistoryContentDesc)
                    }
                    IconButton(onClick = { showHelp = true }) {
                        Icon(Icons.Filled.SmartToy, contentDescription = strings.pomodoro.pomodoroHelpContentDesc)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = phaseLabel(viewModel.phase, strings),
                style = MaterialTheme.typography.titleLarge,
                color = phaseColor
            )
            Spacer(modifier = Modifier.height(24.dp))
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(240.dp),
                    strokeWidth = 10.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    color = phaseColor
                )
                Text(
                    text = formatTime(viewModel.secondsRemaining),
                    style = MaterialTheme.typography.displayMedium
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(4) { index ->
                    val filled = index < viewModel.focusSessionsInCycle
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(
                                if (filled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.reset() }) {
                    Icon(Icons.Filled.Refresh, contentDescription = strings.pomodoro.pomodoroResetContentDesc)
                }
                FilledIconButton(
                    onClick = { if (viewModel.isRunning) viewModel.pause() else viewModel.start() },
                    modifier = Modifier.size(72.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = phaseColor)
                ) {
                    Icon(
                        imageVector = if (viewModel.isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (viewModel.isRunning) {
                            strings.pomodoro.pomodoroPauseContentDesc
                        } else {
                            strings.pomodoro.pomodoroStartContentDesc
                        },
                        modifier = Modifier.size(36.dp)
                    )
                }
                IconButton(onClick = { viewModel.skip() }) {
                    Icon(Icons.Filled.SkipNext, contentDescription = strings.pomodoro.pomodoroSkipContentDesc)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = strings.pomodoro.pomodoroSessionsTodayTemplate.format(sessionsToday),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    if (showHelp) {
        AlertDialog(
            onDismissRequest = { showHelp = false },
            icon = { Icon(Icons.Filled.SmartToy, contentDescription = null) },
            title = { Text(strings.pomodoro.pomodoroHelpTitle) },
            text = { Text(strings.pomodoro.pomodoroHelpText) },
            confirmButton = {
                TextButton(onClick = { showHelp = false }) { Text(strings.pomodoro.pomodoroHelpConfirm) }
            }
        )
    }
}

private fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

private fun phaseLabel(phase: PomodoroPhase, strings: LifeOSStrings): String = when (phase) {
    PomodoroPhase.FOCUS -> strings.pomodoro.pomodoroPhaseFocus
    PomodoroPhase.SHORT_BREAK -> strings.pomodoro.pomodoroPhaseShortBreak
    PomodoroPhase.LONG_BREAK -> strings.pomodoro.pomodoroPhaseLongBreak
}

@Composable
private fun phaseColor(phase: PomodoroPhase): Color = when (phase) {
    PomodoroPhase.FOCUS -> MaterialTheme.colorScheme.primary
    PomodoroPhase.SHORT_BREAK, PomodoroPhase.LONG_BREAK -> MaterialTheme.colorScheme.tertiary
}
