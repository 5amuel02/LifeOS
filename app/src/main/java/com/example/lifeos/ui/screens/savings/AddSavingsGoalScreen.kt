package com.example.lifeos.ui.screens.savings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.money.ThousandsSeparatorVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSavingsGoalScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: AddSavingsGoalViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.savings.savingsAddGoalTitle) },
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
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = viewModel::onNameChange,
                label = { Text(strings.savings.savingsGoalNameLabel) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.targetAmountText,
                onValueChange = viewModel::onTargetAmountChange,
                label = { Text(strings.savings.savingsTargetAmountLabel) },
                prefix = { Text("Rp ") },
                visualTransformation = ThousandsSeparatorVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.monthlyContributionText,
                onValueChange = viewModel::onMonthlyContributionChange,
                label = { Text(strings.savings.savingsMonthlyContributionLabel) },
                prefix = { Text("Rp ") },
                visualTransformation = ThousandsSeparatorVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Column {
                OutlinedTextField(
                    value = viewModel.annualReturnRateText,
                    onValueChange = viewModel::onAnnualReturnRateChange,
                    label = { Text(strings.savings.savingsAnnualReturnLabel) },
                    suffix = { Text("%") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = strings.savings.savingsAnnualReturnHint,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )
            }

            if (viewModel.targetAmount > 0) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = strings.savings.savingsEstimateLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatEstimate(viewModel.estimatedMonths, strings),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
