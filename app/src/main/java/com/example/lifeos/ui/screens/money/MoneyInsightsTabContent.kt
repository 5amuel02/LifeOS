package com.example.lifeos.ui.screens.money

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.data.money.BudgetProgress
import com.example.lifeos.data.money.CategoryBreakdown
import com.example.lifeos.data.money.MonthInsight
import com.example.lifeos.data.money.TransactionCategory
import com.example.lifeos.data.money.TransactionType
import com.example.lifeos.data.money.formatRupiah
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyInsightsTabContent(modifier: Modifier = Modifier) {
    val strings = LocalStrings.current
    val viewModel: MoneyInsightsViewModel = hiltViewModel()
    val streak by viewModel.streak.collectAsStateWithLifecycle()
    val monthInsight by viewModel.monthInsight.collectAsStateWithLifecycle()
    val breakdown by viewModel.categoryBreakdown.collectAsStateWithLifecycle()
    val budgetProgress by viewModel.budgetProgress.collectAsStateWithLifecycle()

    var showSetBudgetDialog by rememberSaveable { mutableStateOf(false) }
    var editingCategory by rememberSaveable { mutableStateOf<TransactionCategory?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { StreakCard(streak, strings) }
        item { InsightCard(monthInsight, strings) }

        item { Text(strings.money.moneyBreakdownTitle, style = MaterialTheme.typography.titleMedium) }
        if (breakdown.isEmpty()) {
            item {
                Text(
                    text = strings.money.moneyBreakdownEmpty,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            item {
                Column {
                    CategoryBreakdownBar(breakdown)
                    CategoryLegendList(breakdown, strings)
                }
            }
        }

        item { Text(strings.money.moneyBudgetSectionTitle, style = MaterialTheme.typography.titleMedium) }
        if (budgetProgress.isEmpty()) {
            item {
                Text(
                    text = strings.money.moneyNoBudgetsYet,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(budgetProgress, key = { it.category.name }) { progress ->
                BudgetProgressRow(
                    progress = progress,
                    strings = strings,
                    onClick = {
                        editingCategory = progress.category
                        showSetBudgetDialog = true
                    },
                    onDeleteClick = { viewModel.removeBudget(progress.category) },
                    modifier = Modifier.animateItem()
                )
            }
        }
        item {
            TextButton(onClick = {
                editingCategory = null
                showSetBudgetDialog = true
            }) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Text(strings.money.moneyAddBudgetLabel)
            }
        }
    }

    if (showSetBudgetDialog) {
        SetBudgetDialog(
            initialCategory = editingCategory,
            existingLimit = budgetProgress.find { it.category == editingCategory }?.limit,
            strings = strings,
            onDismiss = { showSetBudgetDialog = false },
            onConfirm = { category, limit ->
                viewModel.setBudget(category, limit)
                showSetBudgetDialog = false
            }
        )
    }
}

@Composable
private fun StreakCard(streak: Int, strings: LifeOSStrings) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val pulseScale = if (streak > 0) {
                val transition = rememberInfiniteTransition(label = "streakPulse")
                val scale by transition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 650, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "streakPulseScale"
                )
                scale
            } else {
                1f
            }
            Icon(
                imageVector = Icons.Filled.LocalFireDepartment,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.scale(pulseScale)
            )
            Text(
                text = if (streak > 0) strings.money.moneyStreakTemplate.format(streak) else strings.money.moneyStreakEmpty,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun InsightCard(insight: MonthInsight, strings: LifeOSStrings) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (insight.topCategory == null) {
                Text(
                    text = strings.money.moneyInsightNoDataYet,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = strings.money.moneyInsightTopCategoryTemplate.format(
                        insight.topCategory.label(strings),
                        formatRupiah(insight.topCategoryAmount)
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
                val change = insight.percentChange
                if (change != null) {
                    val isUp = change > 0
                    val color = if (isUp) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(
                            imageVector = if (isUp) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (isUp) {
                                strings.money.moneyInsightVsLastMonthUpTemplate.format(abs(change).toInt())
                            } else {
                                strings.money.moneyInsightVsLastMonthDownTemplate.format(abs(change).toInt())
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = color
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryBreakdownBar(breakdown: List<CategoryBreakdown>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        breakdown.forEachIndexed { index, item ->
            key(item.category) {
                val animatedPercentage = remember { Animatable(0f) }
                LaunchedEffect(item.percentage) {
                    animatedPercentage.animateTo(
                        targetValue = item.percentage.coerceAtLeast(0.01f),
                        animationSpec = tween(durationMillis = 700, easing = EaseOutCubic)
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(animatedPercentage.value.coerceAtLeast(0.001f))
                        .fillMaxHeight()
                        .then(if (index < breakdown.lastIndex) Modifier.padding(end = 2.dp) else Modifier)
                        .background(item.category.chartColor())
                )
            }
        }
    }
}

@Composable
private fun CategoryLegendList(breakdown: List<CategoryBreakdown>, strings: LifeOSStrings) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 12.dp)
    ) {
        breakdown.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(item.category.chartColor())
                )
                Text(
                    text = item.category.label(strings),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${(item.percentage * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = formatRupiah(item.amount), style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun BudgetProgressRow(
    progress: BudgetProgress,
    strings: LifeOSStrings,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val ratio = progress.progress.coerceIn(0f, 1f)
    val animatedRatio by animateFloatAsState(
        targetValue = ratio,
        animationSpec = tween(durationMillis = 600, easing = EaseOutCubic),
        label = "budgetProgress"
    )
    val isOver = progress.spent > progress.limit
    val isWarning = !isOver && progress.progress >= 0.8f
    val statusColor = when {
        isOver -> MaterialTheme.colorScheme.error
        isWarning -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }
    val statusIcon = when {
        isOver -> Icons.Filled.Error
        isWarning -> Icons.Filled.WarningAmber
        else -> Icons.Filled.CheckCircle
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = progress.category.icon(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = progress.category.label(strings),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Filled.Delete, contentDescription = strings.money.moneyDeleteBudgetContentDesc)
                }
            }
            LinearProgressIndicator(
                progress = { animatedRatio },
                color = statusColor,
                trackColor = statusColor.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(statusIcon, contentDescription = null, tint = statusColor, modifier = Modifier.size(16.dp))
                Text(
                    text = if (isOver) {
                        strings.money.moneyBudgetOverTemplate.format(formatRupiah(progress.spent - progress.limit))
                    } else {
                        strings.money.moneyBudgetPercentTemplate.format(
                            formatRupiah(progress.spent),
                            formatRupiah(progress.limit),
                            (progress.progress * 100).toInt()
                        )
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = statusColor
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetBudgetDialog(
    initialCategory: TransactionCategory?,
    existingLimit: Long?,
    strings: LifeOSStrings,
    onDismiss: () -> Unit,
    onConfirm: (TransactionCategory, Long) -> Unit,
) {
    val expenseCategories = remember { TransactionCategory.forType(TransactionType.EXPENSE) }
    var selectedCategory by remember { mutableStateOf(initialCategory ?: expenseCategories.first()) }
    var limitText by remember { mutableStateOf(existingLimit?.toString().orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.money.moneySetBudgetTitle) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(expenseCategories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.label(strings)) },
                            leadingIcon = { Icon(category.icon(), contentDescription = null) }
                        )
                    }
                }
                OutlinedTextField(
                    value = limitText,
                    onValueChange = { limitText = it.filter { c -> c.isDigit() } },
                    label = { Text(strings.money.moneyBudgetLimitLabel) },
                    prefix = { Text("Rp ") },
                    visualTransformation = ThousandsSeparatorVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val limit = limitText.toLongOrNull() ?: 0L
                if (limit > 0) onConfirm(selectedCategory, limit)
            }) { Text(strings.shared.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(strings.shared.cancel) }
        }
    )
}
