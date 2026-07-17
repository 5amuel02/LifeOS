package com.example.lifeos.ui.screens.jadwal

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.material3.rememberDatePickerState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.core.audio.rememberFeedbackSounds
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.data.jadwal.JadwalEntity
import com.example.lifeos.ui.screens.common.PlaceholderScreen
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun formatMinuteOfDay(minuteOfDay: Int): String =
    "%02d:%02d".format(minuteOfDay / 60, minuteOfDay % 60)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: JadwalViewModel = viewModel()
    val sounds = rememberFeedbackSounds()
    val items by viewModel.items.collectAsStateWithLifecycle()

    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var newTitle by rememberSaveable { mutableStateOf("") }
    var newDateEpochDay by rememberSaveable { mutableStateOf(LocalDate.now().toEpochDay()) }
    var newMinuteOfDay by rememberSaveable { mutableStateOf<Int?>(null) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var itemIdPendingDelete by rememberSaveable { mutableStateOf<Long?>(null) }

    val dateFormatter = remember(strings.localeTag) {
        DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.forLanguageTag(strings.localeTag))
    }

    fun dismissAddDialog() {
        showAddDialog = false
        newTitle = ""
        newDateEpochDay = LocalDate.now().toEpochDay()
        newMinuteOfDay = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.jadwal.jadwalTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.shared.back)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = strings.jadwal.jadwalAddContentDesc)
            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(modifier = Modifier.padding(padding)) {
                PlaceholderScreen(
                    title = strings.jadwal.jadwalTitle,
                    subtitle = strings.jadwal.jadwalEmptySubtitle,
                    icon = Icons.Filled.CalendarMonth
                )
            }
        } else {
            val grouped = items.groupBy { it.dateEpochDay }.toSortedMap()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                grouped.forEach { (dateEpochDay, itemsForDay) ->
                    item(key = "header_$dateEpochDay") {
                        Text(
                            text = LocalDate.ofEpochDay(dateEpochDay).format(dateFormatter),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }
                    items(itemsForDay, key = { it.id }) { entry ->
                        JadwalListItem(
                            item = entry,
                            strings = strings,
                            onToggle = {
                                if (!entry.isCompleted) sounds.playCheck()
                                viewModel.toggleCompleted(entry)
                            },
                            onDeleteClick = { itemIdPendingDelete = entry.id }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { dismissAddDialog() },
            title = { Text(strings.jadwal.jadwalAddDialogTitle) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text(strings.jadwal.jadwalTitleLabel) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = null)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = strings.jadwal.jadwalDateLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = LocalDate.ofEpochDay(newDateEpochDay).format(dateFormatter),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTimePicker = true }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Schedule, contentDescription = null)
                        Text(
                            text = newMinuteOfDay?.let(::formatMinuteOfDay) ?: strings.jadwal.jadwalTimeNoneLabel,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        if (newMinuteOfDay != null) {
                            IconButton(onClick = { newMinuteOfDay = null }) {
                                Icon(Icons.Filled.Close, contentDescription = strings.jadwal.jadwalClearTime)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addItem(newTitle, LocalDate.ofEpochDay(newDateEpochDay), newMinuteOfDay)
                        dismissAddDialog()
                    },
                    enabled = newTitle.isNotBlank()
                ) { Text(strings.jadwal.jadwalAdd) }
            },
            dismissButton = {
                TextButton(onClick = { dismissAddDialog() }) { Text(strings.shared.cancel) }
            }
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = LocalDate.ofEpochDay(newDateEpochDay)
                .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        newDateEpochDay = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate().toEpochDay()
                    }
                    showDatePicker = false
                }) { Text(strings.habitSummary.habitSummaryPick) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(strings.shared.cancel) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        JadwalTimePickerDialog(
            initialMinuteOfDay = newMinuteOfDay,
            showClear = newMinuteOfDay != null,
            strings = strings,
            onConfirm = { minute ->
                newMinuteOfDay = minute
                showTimePicker = false
            },
            onClear = {
                newMinuteOfDay = null
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

    val idToDelete = itemIdPendingDelete
    if (idToDelete != null) {
        AlertDialog(
            onDismissRequest = { itemIdPendingDelete = null },
            title = { Text(strings.jadwal.jadwalDeleteDialogTitle) },
            text = { Text(strings.jadwal.jadwalDeleteDialogText) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteItem(idToDelete)
                    itemIdPendingDelete = null
                }) { Text(strings.shared.delete) }
            },
            dismissButton = {
                TextButton(onClick = { itemIdPendingDelete = null }) { Text(strings.shared.cancel) }
            }
        )
    }
}

@Composable
private fun JadwalListItem(
    item: JadwalEntity,
    strings: LifeOSStrings,
    onToggle: () -> Unit,
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
            LaunchedEffect(item.isCompleted) {
                if (item.isCompleted) {
                    checkScale.animateTo(1.3f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                    checkScale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                }
            }
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggle() },
                modifier = Modifier.scale(checkScale.value)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                item.minuteOfDay?.let { minute ->
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
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = strings.jadwal.jadwalDeleteContentDesc)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JadwalTimePickerDialog(
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
        title = { Text(strings.jadwal.jadwalTimeDialogTitle) },
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
                    TextButton(onClick = onClear) { Text(strings.jadwal.jadwalClearTime) }
                }
                TextButton(onClick = onDismiss) { Text(strings.shared.cancel) }
            }
        }
    )
}
