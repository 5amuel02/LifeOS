package com.example.lifeos.ui.screens.habit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lifeos.core.audio.rememberFeedbackSounds
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.common.PlaceholderScreen
import java.time.LocalTime

private fun formatMinuteOfDay(minuteOfDay: Int): String =
    "%02d:%02d".format(minuteOfDay / 60, minuteOfDay % 60)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(onOpenSummary: () -> Unit = {}) {
    val strings = LocalStrings.current
    val viewModel: HabitViewModel = hiltViewModel()
    val sounds = rememberFeedbackSounds()
    val habits by viewModel.habits.collectAsStateWithLifecycle()
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var newHabitName by rememberSaveable { mutableStateOf("") }
    var newHabitReminder by rememberSaveable { mutableStateOf<Int?>(null) }
    var showAddReminderPicker by rememberSaveable { mutableStateOf(false) }
    var habitIdPendingDelete by rememberSaveable { mutableStateOf<Long?>(null) }
    var habitPendingReminderEdit by remember { mutableStateOf<HabitUi?>(null) }

    fun dismissAddDialog() {
        showAddDialog = false
        newHabitName = ""
        newHabitReminder = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.habit.habitTitle) },
                actions = {
                    IconButton(onClick = onOpenSummary) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = strings.habit.habitSummaryContentDesc)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = strings.habit.habitAddContentDesc)
            }
        }
    ) { padding ->
        if (habits.isEmpty()) {
            Box(modifier = Modifier.padding(padding)) {
                PlaceholderScreen(
                    title = strings.habit.habitTitle,
                    subtitle = strings.habit.habitEmptySubtitle,
                    icon = Icons.Filled.CheckCircle
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(habits, key = { it.id }) { habit ->
                    HabitListItem(
                        habit = habit,
                        strings = strings,
                        onToggle = {
                            if (!habit.isDoneToday) sounds.playCheck()
                            viewModel.toggleToday(habit)
                        },
                        onSetReminder = { habitPendingReminderEdit = habit },
                        onDeleteClick = { habitIdPendingDelete = habit.id }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { dismissAddDialog() },
            title = { Text(strings.habit.habitAddDialogTitle) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newHabitName,
                        onValueChange = { newHabitName = it },
                        label = { Text(strings.habit.habitNameLabel) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAddReminderPicker = true }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Alarm, contentDescription = null)
                        Text(
                            text = newHabitReminder?.let(::formatMinuteOfDay)
                                ?: strings.habitReminder.habitReminderNoneLabel,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        if (newHabitReminder != null) {
                            IconButton(onClick = { newHabitReminder = null }) {
                                Icon(Icons.Filled.Close, contentDescription = strings.habitReminder.habitReminderClear)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.addHabit(newHabitName, newHabitReminder)
                    dismissAddDialog()
                }) { Text(strings.habit.habitAdd) }
            },
            dismissButton = {
                TextButton(onClick = { dismissAddDialog() }) { Text(strings.shared.cancel) }
            }
        )
    }

    if (showAddReminderPicker) {
        ReminderTimePickerDialog(
            initialMinuteOfDay = newHabitReminder,
            showClear = newHabitReminder != null,
            strings = strings,
            onConfirm = { minute ->
                newHabitReminder = minute
                showAddReminderPicker = false
            },
            onClear = {
                newHabitReminder = null
                showAddReminderPicker = false
            },
            onDismiss = { showAddReminderPicker = false }
        )
    }

    val editingHabit = habitPendingReminderEdit
    if (editingHabit != null) {
        ReminderTimePickerDialog(
            initialMinuteOfDay = editingHabit.reminderMinuteOfDay,
            showClear = editingHabit.reminderMinuteOfDay != null,
            strings = strings,
            onConfirm = { minute ->
                viewModel.updateReminder(editingHabit, minute)
                habitPendingReminderEdit = null
            },
            onClear = {
                viewModel.updateReminder(editingHabit, null)
                habitPendingReminderEdit = null
            },
            onDismiss = { habitPendingReminderEdit = null }
        )
    }

    val idToDelete = habitIdPendingDelete
    if (idToDelete != null) {
        AlertDialog(
            onDismissRequest = { habitIdPendingDelete = null },
            title = { Text(strings.habit.habitDeleteDialogTitle) },
            text = { Text(strings.habit.habitDeleteDialogText) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteHabit(idToDelete)
                    habitIdPendingDelete = null
                }) { Text(strings.shared.delete) }
            },
            dismissButton = {
                TextButton(onClick = { habitIdPendingDelete = null }) { Text(strings.shared.cancel) }
            }
        )
    }
}

@Composable
private fun HabitListItem(
    habit: HabitUi,
    strings: LifeOSStrings,
    onToggle: () -> Unit,
    onSetReminder: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val checkScale = remember { Animatable(1f) }
            LaunchedEffect(habit.isDoneToday) {
                if (habit.isDoneToday) {
                    checkScale.animateTo(1.3f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                    checkScale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                }
            }
            Checkbox(
                checked = habit.isDoneToday,
                onCheckedChange = { onToggle() },
                modifier = Modifier.scale(checkScale.value)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (habit.isDoneToday) TextDecoration.LineThrough else TextDecoration.None
                )
                if (habit.streak > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocalFireDepartment,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = strings.habit.habitStreakTemplate.format(habit.streak),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                habit.reminderMinuteOfDay?.let { minute ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = formatMinuteOfDay(minute),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            IconButton(onClick = onSetReminder) {
                Icon(
                    imageVector = Icons.Filled.Alarm,
                    contentDescription = strings.habitReminder.habitReminderSetContentDesc,
                    tint = if (habit.reminderMinuteOfDay != null) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = strings.habit.habitDeleteContentDesc)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderTimePickerDialog(
    initialMinuteOfDay: Int?,
    showClear: Boolean,
    strings: LifeOSStrings,
    onConfirm: (Int) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit,
) {
    val fallback = remember { LocalTime.now() }
    val initial = initialMinuteOfDay ?: (fallback.hour * 60 + fallback.minute)
    val timeState = rememberTimePickerState(
        initialHour = initial / 60,
        initialMinute = initial % 60,
        is24Hour = true,
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.habitReminder.habitReminderDialogTitle) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = timeState)
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(timeState.hour * 60 + timeState.minute) }) {
                Text(strings.shared.save)
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                if (showClear) {
                    TextButton(onClick = onClear) { Text(strings.habitReminder.habitReminderClear) }
                }
                TextButton(onClick = onDismiss) { Text(strings.shared.cancel) }
            }
        }
    )
}
