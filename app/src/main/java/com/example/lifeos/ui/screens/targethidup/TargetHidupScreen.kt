package com.example.lifeos.ui.screens.targethidup

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lifeos.core.audio.rememberFeedbackSounds
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.data.targethidup.TargetHidupEntity
import com.example.lifeos.ui.screens.common.PlaceholderScreen
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetHidupScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: TargetHidupViewModel = hiltViewModel()
    val sounds = rememberFeedbackSounds()
    val goals by viewModel.goals.collectAsStateWithLifecycle()

    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var newTitle by rememberSaveable { mutableStateOf("") }
    var newTargetDateEpochDay by rememberSaveable { mutableStateOf<Long?>(null) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var goalIdPendingDelete by rememberSaveable { mutableStateOf<Long?>(null) }
    var goalPendingProgressEdit by remember { mutableStateOf<TargetHidupEntity?>(null) }

    val dateFormatter = remember(strings.localeTag) {
        DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag(strings.localeTag))
    }

    fun dismissAddDialog() {
        showAddDialog = false
        newTitle = ""
        newTargetDateEpochDay = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.targetHidup.targetHidupTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.shared.back)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = strings.targetHidup.targetHidupAddContentDesc)
            }
        }
    ) { padding ->
        if (goals.isEmpty()) {
            Box(modifier = Modifier.padding(padding)) {
                PlaceholderScreen(
                    title = strings.targetHidup.targetHidupTitle,
                    subtitle = strings.targetHidup.targetHidupEmptySubtitle,
                    icon = Icons.Filled.Flag
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
                items(goals, key = { it.id }) { goal ->
                    TargetHidupCard(
                        goal = goal,
                        strings = strings,
                        dateFormatter = dateFormatter,
                        onClick = { goalPendingProgressEdit = goal },
                        onDeleteClick = { goalIdPendingDelete = goal.id }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { dismissAddDialog() },
            title = { Text(strings.targetHidup.targetHidupAddDialogTitle) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text(strings.targetHidup.targetHidupTitleLabel) },
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
                        Text(
                            text = newTargetDateEpochDay?.let { LocalDate.ofEpochDay(it).format(dateFormatter) }
                                ?: strings.targetHidup.targetHidupTargetDateNoneLabel,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        if (newTargetDateEpochDay != null) {
                            IconButton(onClick = { newTargetDateEpochDay = null }) {
                                Icon(Icons.Filled.Close, contentDescription = strings.targetHidup.targetHidupTargetDateNoneLabel)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addGoal(newTitle, newTargetDateEpochDay)
                        dismissAddDialog()
                    },
                    enabled = newTitle.isNotBlank()
                ) { Text(strings.targetHidup.targetHidupAdd) }
            },
            dismissButton = {
                TextButton(onClick = { dismissAddDialog() }) { Text(strings.shared.cancel) }
            }
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = (newTargetDateEpochDay?.let { LocalDate.ofEpochDay(it) } ?: LocalDate.now())
                .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        newTargetDateEpochDay = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate().toEpochDay()
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

    val editingGoal = goalPendingProgressEdit
    if (editingGoal != null) {
        ProgressEditDialog(
            goal = editingGoal,
            strings = strings,
            onConfirm = { percent ->
                if (percent >= 100 && editingGoal.progressPercent < 100) {
                    sounds.playComplete()
                }
                viewModel.updateProgress(editingGoal.id, percent)
                goalPendingProgressEdit = null
            },
            onDismiss = { goalPendingProgressEdit = null }
        )
    }

    val idToDelete = goalIdPendingDelete
    if (idToDelete != null) {
        AlertDialog(
            onDismissRequest = { goalIdPendingDelete = null },
            title = { Text(strings.targetHidup.targetHidupDeleteDialogTitle) },
            text = { Text(strings.targetHidup.targetHidupDeleteDialogText) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteGoal(idToDelete)
                    goalIdPendingDelete = null
                }) { Text(strings.shared.delete) }
            },
            dismissButton = {
                TextButton(onClick = { goalIdPendingDelete = null }) { Text(strings.shared.cancel) }
            }
        )
    }
}

@Composable
private fun TargetHidupCard(
    goal: TargetHidupEntity,
    strings: LifeOSStrings,
    dateFormatter: DateTimeFormatter,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val isCompleted = goal.progressPercent >= 100
    val animatedProgress by animateFloatAsState(
        targetValue = goal.progressPercent / 100f,
        animationSpec = tween(durationMillis = 600),
        label = "targetHidupProgress"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = goal.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Filled.Delete, contentDescription = strings.targetHidup.targetHidupDeleteContentDesc)
                }
            }
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isCompleted) {
                        strings.targetHidup.targetHidupCompletedLabel
                    } else {
                        strings.targetHidup.targetHidupProgressTemplate.format(goal.progressPercent)
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                goal.targetDateEpochDay?.let { epochDay ->
                    val daysLeft = epochDay - LocalDate.now().toEpochDay()
                    val label = if (daysLeft < 0) {
                        strings.targetHidup.targetHidupOverdueLabel
                    } else {
                        strings.targetHidup.targetHidupDaysLeftTemplate.format(daysLeft)
                    }
                    Text(
                        text = "${LocalDate.ofEpochDay(epochDay).format(dateFormatter)} · $label",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (daysLeft < 0 && !isCompleted) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgressEditDialog(
    goal: TargetHidupEntity,
    strings: LifeOSStrings,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var sliderValue by remember(goal.id) { mutableFloatStateOf(goal.progressPercent.toFloat()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.targetHidup.targetHidupEditDialogTitle) },
        text = {
            Column {
                Text(goal.title, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = strings.targetHidup.targetHidupProgressTemplate.format(sliderValue.toInt()),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    valueRange = 0f..100f,
                    steps = 19,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(sliderValue.toInt()) }) { Text(strings.shared.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(strings.shared.cancel) }
        }
    )
}
