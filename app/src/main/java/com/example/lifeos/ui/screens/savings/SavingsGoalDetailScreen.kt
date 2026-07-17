package com.example.lifeos.ui.screens.savings

import android.app.Application
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.core.audio.rememberFeedbackSounds
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.data.money.formatRupiah
import com.example.lifeos.data.savings.SavingsDepositEntity
import com.example.lifeos.data.savings.estimateMonthsToTarget
import com.example.lifeos.ui.screens.money.AnimatedRupiahText
import com.example.lifeos.ui.screens.money.ThousandsSeparatorVisualTransformation
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGoalDetailScreen(goalId: Long, onBack: () -> Unit) {
    val strings = LocalStrings.current
    val context = LocalContext.current
    val viewModel: SavingsGoalDetailViewModel = viewModel(
        factory = SavingsGoalDetailViewModelFactory(context.applicationContext as Application, goalId)
    )
    val goal by viewModel.goal.collectAsStateWithLifecycle()
    val deposits by viewModel.deposits.collectAsStateWithLifecycle()
    val sounds = rememberFeedbackSounds()

    var showDeleteGoalConfirm by rememberSaveable { mutableStateOf(false) }
    var depositPendingDelete by rememberSaveable { mutableStateOf<Long?>(null) }
    var showAddDepositDialog by rememberSaveable { mutableStateOf(false) }
    var celebrated by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(goal?.currentAmount, goal?.targetAmount) {
        val currentGoal = goal ?: return@LaunchedEffect
        val isCompleted = currentGoal.targetAmount > 0 && currentGoal.currentAmount >= currentGoal.targetAmount
        if (isCompleted && !celebrated) {
            sounds.playComplete()
            celebrated = true
        }
    }

    val dateFormatter = remember(strings.localeTag) {
        DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag(strings.localeTag))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(goal?.name.orEmpty()) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.shared.back)
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteGoalConfirm = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = strings.savings.savingsDeleteGoalContentDesc)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDepositDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = strings.savings.savingsAddDepositContentDesc)
            }
        }
    ) { padding ->
        val currentGoal = goal
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (currentGoal != null) {
                item {
                    val progress = if (currentGoal.targetAmount == 0L) {
                        0f
                    } else {
                        (currentGoal.currentAmount.toFloat() / currentGoal.targetAmount.toFloat()).coerceIn(0f, 1f)
                    }
                    val animatedProgress by animateFloatAsState(
                        targetValue = progress,
                        animationSpec = tween(durationMillis = 600, easing = EaseOutCubic),
                        label = "savingsGoalDetailProgress"
                    )
                    val months = estimateMonthsToTarget(
                        currentAmount = currentGoal.currentAmount,
                        targetAmount = currentGoal.targetAmount,
                        monthlyContribution = currentGoal.monthlyContribution,
                        annualReturnRatePercent = currentGoal.annualReturnRatePercent
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row {
                                AnimatedRupiahText(
                                    amount = currentGoal.currentAmount,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = " / ${formatRupiah(currentGoal.targetAmount)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            LinearProgressIndicator(
                                progress = { animatedProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp)
                            )
                            Text(
                                text = strings.savings.savingsEstimateLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                            Text(
                                text = formatEstimate(months, strings),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            item {
                Text(text = strings.savings.savingsDepositHistoryTitle, style = MaterialTheme.typography.titleMedium)
            }

            if (deposits.isEmpty()) {
                item {
                    Text(
                        text = strings.savings.savingsNoDepositsYet,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(deposits, key = { it.id }) { deposit ->
                    DepositRow(
                        deposit = deposit,
                        dateText = LocalDate.ofEpochDay(deposit.dateEpochDay).format(dateFormatter),
                        onDeleteClick = { depositPendingDelete = deposit.id },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }

    if (showDeleteGoalConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteGoalConfirm = false },
            title = { Text(strings.savings.savingsDeleteGoalDialogTitle) },
            text = { Text(strings.savings.savingsDeleteGoalDialogText) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteGoalConfirm = false
                    viewModel.deleteGoal(onBack)
                }) { Text(strings.shared.delete) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteGoalConfirm = false }) { Text(strings.shared.cancel) }
            }
        )
    }

    val idToDelete = depositPendingDelete
    if (idToDelete != null) {
        val deposit = deposits.find { it.id == idToDelete }
        AlertDialog(
            onDismissRequest = { depositPendingDelete = null },
            title = { Text(strings.savings.savingsDeleteDepositDialogTitle) },
            text = { Text(strings.savings.savingsDeleteDepositDialogText) },
            confirmButton = {
                TextButton(onClick = {
                    deposit?.let { viewModel.deleteDeposit(it) }
                    depositPendingDelete = null
                }) { Text(strings.shared.delete) }
            },
            dismissButton = {
                TextButton(onClick = { depositPendingDelete = null }) { Text(strings.shared.cancel) }
            }
        )
    }

    if (showAddDepositDialog) {
        AddDepositDialog(
            onDismiss = { showAddDepositDialog = false },
            onConfirm = { amount, date ->
                viewModel.addDeposit(amount, date)
                showAddDepositDialog = false
            }
        )
    }
}

@Composable
private fun DepositRow(
    deposit: SavingsDepositEntity,
    dateText: String,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = formatRupiah(deposit.amount), style = MaterialTheme.typography.titleMedium)
                Text(text = dateText, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = strings.shared.delete)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddDepositDialog(onDismiss: () -> Unit, onConfirm: (Long, LocalDate) -> Unit) {
    val strings = LocalStrings.current
    var amountText by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val dateFormatter = remember(strings.localeTag) {
        DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag(strings.localeTag))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.savings.savingsAddDepositTitle) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { c -> c.isDigit() } },
                    label = { Text(strings.savings.savingsDepositAmountLabel) },
                    prefix = { Text("Rp ") },
                    visualTransformation = ThousandsSeparatorVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(text = date.format(dateFormatter), modifier = Modifier.weight(1f))
                    TextButton(onClick = { showDatePicker = true }) { Text(strings.habitSummary.habitSummaryPick) }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val amount = amountText.toLongOrNull() ?: 0L
                if (amount > 0) onConfirm(amount, date)
            }) { Text(strings.shared.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(strings.shared.cancel) }
        }
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
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
}
