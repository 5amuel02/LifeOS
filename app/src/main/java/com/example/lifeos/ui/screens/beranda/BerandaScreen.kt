package com.example.lifeos.ui.screens.beranda

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings
import java.time.LocalTime

private data class QuickStat(val icon: ImageVector, val label: String, val value: String, val onClick: () -> Unit)

@Composable
fun BerandaScreen(
    onOpenNotes: () -> Unit = {},
    onOpenHabit: () -> Unit = {},
    onOpenPomodoro: () -> Unit = {},
) {
    val strings = LocalStrings.current
    val viewModel: BerandaViewModel = hiltViewModel()
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val latestNote = notes.firstOrNull()
    val habitSummary by viewModel.habitSummary.collectAsStateWithLifecycle()
    val pomodoroSessionsToday by viewModel.pomodoroSessionsToday.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(text = greetingForNow(strings), style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = strings.beranda.berandaSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenHabit),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { habitSummary.progress },
                            modifier = Modifier.size(72.dp),
                            strokeWidth = 6.dp,
                            trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f)
                        )
                        Text(
                            text = "${(habitSummary.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Column {
                        Text(
                            text = strings.beranda.berandaHabitCardTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = strings.beranda.berandaHabitProgressTemplate.format(
                                habitSummary.doneToday,
                                habitSummary.total
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        item {
            MotivationBanner(
                stickman = { ExercisingStickman() },
                text = strings.beranda.berandaHabitMotivationText
            )
        }

        item {
            val stats = listOf(
                QuickStat(
                    Icons.AutoMirrored.Filled.Notes,
                    strings.beranda.berandaStatNotesLabel,
                    strings.beranda.berandaStatNotesValueTemplate.format(notes.size),
                    onOpenNotes
                ),
                QuickStat(
                    Icons.Filled.Timer,
                    strings.beranda.berandaStatPomodoroLabel,
                    strings.beranda.berandaStatPomodoroValueTemplate.format(pomodoroSessionsToday),
                    onOpenPomodoro
                ),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                stats.forEach { stat ->
                    QuickStatCard(stat = stat, modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            MotivationBanner(
                stickman = { StudyingStickman() },
                text = strings.beranda.berandaPomodoroMotivationText
            )
        }

        item {
            Text(text = strings.beranda.berandaActivitiesTitle, style = MaterialTheme.typography.titleMedium)
        }

        val notesActivityText = if (latestNote != null) {
            strings.beranda.berandaNotesActivityTemplate.format(latestNote.title.ifBlank { strings.shared.untitledNote })
        } else {
            strings.beranda.berandaNotesActivityEmpty
        }
        val habitActivityText = if (habitSummary.total == 0) {
            strings.beranda.berandaHabitActivityEmpty
        } else {
            strings.beranda.berandaHabitActivityTemplate.format(habitSummary.doneToday, habitSummary.total)
        }
        val pomodoroActivityText = if (pomodoroSessionsToday == 0) {
            strings.beranda.berandaPomodoroActivityEmpty
        } else {
            strings.beranda.berandaPomodoroActivityTemplate.format(pomodoroSessionsToday)
        }

        items(
            listOf(
                Triple(Icons.Filled.CheckCircle, habitActivityText, onOpenHabit),
                Triple(Icons.AutoMirrored.Filled.Notes, notesActivityText, onOpenNotes),
                Triple(Icons.Filled.Timer, pomodoroActivityText, onOpenPomodoro),
            )
        ) { (icon, text, onClick) ->
            Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickStatCard(stat: QuickStat, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(1.3f)
            .clickable(onClick = stat.onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(imageVector = stat.icon, contentDescription = stat.label, tint = MaterialTheme.colorScheme.primary)
            Column {
                Text(text = stat.label, style = MaterialTheme.typography.labelLarge)
                Text(
                    text = stat.value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
private fun MotivationBanner(stickman: @Composable () -> Unit, text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            stickman()
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun greetingForNow(strings: LifeOSStrings): String {
    val hour = LocalTime.now().hour
    return when (hour) {
        in 4..10 -> strings.beranda.berandaGreetingMorning
        in 11..14 -> strings.beranda.berandaGreetingAfternoon
        in 15..18 -> strings.beranda.berandaGreetingEvening
        else -> strings.beranda.berandaGreetingNight
    }
}
