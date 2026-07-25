package com.example.lifeos.ui.screens.savings

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.data.money.formatRupiah
import com.example.lifeos.data.savings.SavingsGoalEntity
import com.example.lifeos.data.savings.estimateMonthsToTarget
import com.example.lifeos.ui.screens.common.PlaceholderScreen
import com.example.lifeos.ui.screens.money.AnimatedRupiahText

@Composable
fun SavingsTabContent(modifier: Modifier = Modifier, onOpenGoal: (Long) -> Unit) {
    val strings = LocalStrings.current
    val viewModel: SavingsViewModel = hiltViewModel()
    val goals by viewModel.goals.collectAsStateWithLifecycle()

    if (goals.isEmpty()) {
        Box(modifier = modifier.fillMaxSize()) {
            PlaceholderScreen(
                title = strings.savings.savingsEmptyTitle,
                subtitle = strings.savings.savingsEmptySubtitle,
                icon = Icons.Filled.Savings
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(goals, key = { it.id }) { goal ->
                SavingsGoalCard(
                    goal = goal,
                    onClick = { onOpenGoal(goal.id) },
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}

@Composable
private fun SavingsGoalCard(goal: SavingsGoalEntity, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val strings = LocalStrings.current
    val progress = if (goal.targetAmount == 0L) 0f else (goal.currentAmount.toFloat() / goal.targetAmount.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600, easing = EaseOutCubic),
        label = "savingsGoalProgress"
    )
    val months = estimateMonthsToTarget(
        currentAmount = goal.currentAmount,
        targetAmount = goal.targetAmount,
        monthlyContribution = goal.monthlyContribution,
        annualReturnRatePercent = goal.annualReturnRatePercent
    )

    Card(modifier = modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = goal.name, style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.padding(top = 2.dp)) {
                AnimatedRupiahText(
                    amount = goal.currentAmount,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = " / ${formatRupiah(goal.targetAmount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )
            Text(
                text = formatEstimate(months, strings),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
