package com.example.lifeos.ui.screens.money

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.data.money.TransactionEntity
import com.example.lifeos.data.money.TransactionType
import com.example.lifeos.data.money.formatRupiah
import com.example.lifeos.ui.screens.common.PlaceholderScreen
import com.example.lifeos.ui.screens.savings.SavingsTabContent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyManagerScreen(
    onBack: () -> Unit,
    onAddTransaction: () -> Unit,
    onAddSavingsGoal: () -> Unit,
    onOpenSavingsGoal: (Long) -> Unit,
) {
    val strings = LocalStrings.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(strings.lainnya.moduleMoneyManager) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.shared.back)
                        }
                    }
                )
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text(strings.money.moneyTabTransactions) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text(strings.money.moneyTabInsights) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text(strings.money.moneyTabSavings) }
                    )
                }
            }
        },
        floatingActionButton = {
            when (selectedTab) {
                0 -> FloatingActionButton(onClick = onAddTransaction) {
                    Icon(Icons.Filled.Add, contentDescription = strings.money.moneyAddContentDesc)
                }
                2 -> FloatingActionButton(onClick = onAddSavingsGoal) {
                    Icon(Icons.Filled.Add, contentDescription = strings.savings.savingsAddContentDesc)
                }
            }
        }
    ) { padding ->
        AnimatedContent(
            targetState = selectedTab,
            label = "moneyManagerTab",
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { tab ->
            when (tab) {
                0 -> TransactionsTabContent(modifier = Modifier.padding(padding))
                1 -> MoneyInsightsTabContent(modifier = Modifier.padding(padding))
                else -> SavingsTabContent(modifier = Modifier.padding(padding), onOpenGoal = onOpenSavingsGoal)
            }
        }
    }
}

@Composable
private fun TransactionsTabContent(modifier: Modifier = Modifier) {
    val strings = LocalStrings.current
    val viewModel: MoneyManagerViewModel = viewModel()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val summary by viewModel.summary.collectAsStateWithLifecycle()
    var transactionIdPendingDelete by rememberSaveable { mutableStateOf<Long?>(null) }

    val dateFormatter = remember(strings.localeTag) {
        DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.forLanguageTag(strings.localeTag))
    }
    val groupedTransactions = remember(transactions) {
        transactions.groupBy { it.dateEpochDay }
    }

    Column(modifier = modifier.fillMaxSize()) {
        SummaryCard(
            incomeLabel = strings.money.moneySummaryIncome,
            expenseLabel = strings.money.moneySummaryExpense,
            balanceLabel = strings.money.moneySummaryBalance,
            totalIncome = summary.totalIncome,
            totalExpense = summary.totalExpense,
            balance = summary.balance,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )

        if (transactions.isEmpty()) {
            Box(modifier = Modifier.weight(1f)) {
                PlaceholderScreen(
                    title = strings.money.moneyEmptyTitle,
                    subtitle = strings.money.moneyEmptySubtitle,
                    icon = Icons.Filled.AttachMoney
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                groupedTransactions.forEach { (epochDay, dayTransactions) ->
                    item(key = "header_$epochDay") {
                        Text(
                            text = LocalDate.ofEpochDay(epochDay).format(dateFormatter),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }
                    items(dayTransactions, key = { it.id }) { transaction ->
                        TransactionRow(
                            transaction = transaction,
                            onDeleteClick = { transactionIdPendingDelete = transaction.id },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }

    val idToDelete = transactionIdPendingDelete
    if (idToDelete != null) {
        AlertDialog(
            onDismissRequest = { transactionIdPendingDelete = null },
            title = { Text(strings.money.moneyDeleteDialogTitle) },
            text = { Text(strings.money.moneyDeleteDialogText) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTransaction(idToDelete)
                    transactionIdPendingDelete = null
                }) { Text(strings.shared.delete) }
            },
            dismissButton = {
                TextButton(onClick = { transactionIdPendingDelete = null }) { Text(strings.shared.cancel) }
            }
        )
    }
}

@Composable
private fun SummaryCard(
    incomeLabel: String,
    expenseLabel: String,
    balanceLabel: String,
    totalIncome: Long,
    totalExpense: Long,
    balance: Long,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = balanceLabel,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            AnimatedRupiahText(
                amount = balance,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = incomeLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    AnimatedRupiahText(
                        amount = totalIncome,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = expenseLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.End
                    )
                    AnimatedRupiahText(
                        amount = totalExpense,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionRow(transaction: TransactionEntity, onDeleteClick: () -> Unit, modifier: Modifier = Modifier) {
    val strings = LocalStrings.current
    val category = com.example.lifeos.data.money.TransactionCategory.fromName(transaction.category)
    val isIncome = transaction.type == TransactionType.INCOME.name
    val amountColor = if (isIncome) incomeColor() else MaterialTheme.colorScheme.error

    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = category.icon(),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = category.label(strings), style = MaterialTheme.typography.titleMedium)
                if (transaction.note.isNotBlank()) {
                    Text(
                        text = transaction.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = "${if (isIncome) "+" else "-"} ${formatRupiah(transaction.amount)}",
                style = MaterialTheme.typography.titleMedium,
                color = amountColor
            )
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = strings.shared.delete)
            }
        }
    }
}
