package com.example.lifeos.ui.screens.money

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.data.money.TransactionCategory
import com.example.lifeos.data.money.TransactionType
import com.example.lifeos.data.money.formatRupiah
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: AddTransactionViewModel = viewModel()
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    val dateFormatter = remember(strings.localeTag) {
        DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag(strings.localeTag))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.money.moneyAddTransactionTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.shared.back)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.save(onBack) },
                        enabled = viewModel.canSave
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = strings.shared.save)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = viewModel.type == TransactionType.EXPENSE,
                    onClick = { viewModel.onTypeSelected(TransactionType.EXPENSE) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                ) { Text(strings.money.moneySummaryExpense) }
                SegmentedButton(
                    selected = viewModel.type == TransactionType.INCOME,
                    onClick = { viewModel.onTypeSelected(TransactionType.INCOME) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                ) { Text(strings.money.moneySummaryIncome) }
            }

            OutlinedTextField(
                value = viewModel.amountText,
                onValueChange = viewModel::onAmountChange,
                label = { Text(strings.money.moneyAmountLabel) },
                prefix = { Text("Rp ") },
                visualTransformation = ThousandsSeparatorVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Column {
                Text(text = strings.money.moneyCategoryLabel, style = MaterialTheme.typography.labelLarge)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    items(TransactionCategory.forType(viewModel.type)) { category ->
                        FilterChip(
                            selected = viewModel.category == category,
                            onClick = { viewModel.onCategorySelected(category) },
                            label = { Text(category.label(strings)) },
                            leadingIcon = { Icon(category.icon(), contentDescription = null) }
                        )
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.money.moneyDateLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(text = viewModel.date.format(dateFormatter), style = MaterialTheme.typography.titleMedium)
                    }
                    TextButton(onClick = { showDatePicker = true }) { Text(strings.habitSummary.habitSummaryPick) }
                }
            }

            OutlinedTextField(
                value = viewModel.note,
                onValueChange = viewModel::onNoteChange,
                label = { Text(strings.money.moneyNoteLabel) },
                placeholder = { Text(strings.money.moneyNotePlaceholder) },
                modifier = Modifier.fillMaxWidth()
            )

            if (viewModel.amount > 0) {
                Text(
                    text = formatRupiah(viewModel.amount),
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (viewModel.type == TransactionType.INCOME) {
                        incomeColor()
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = viewModel.date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val picked = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                        viewModel.onDateSelected(picked)
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
