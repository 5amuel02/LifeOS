package com.example.lifeos.data.money

import java.time.LocalDate
import java.time.YearMonth

data class CategoryBreakdown(
    val category: TransactionCategory,
    val amount: Long,
    val percentage: Float,
)

data class MonthInsight(
    val topCategory: TransactionCategory?,
    val topCategoryAmount: Long,
    val totalThisMonth: Long,
    val totalLastMonth: Long,
) {
    val percentChange: Float?
        get() = if (totalLastMonth == 0L) null else ((totalThisMonth - totalLastMonth) * 100f) / totalLastMonth
}

data class BudgetProgress(
    val category: TransactionCategory,
    val limit: Long,
    val spent: Long,
) {
    val progress: Float get() = if (limit == 0L) 0f else (spent.toFloat() / limit.toFloat())
}

fun calculateLoggingStreak(transactionDates: Set<Long>): Int {
    val today = LocalDate.now().toEpochDay()
    val anchor = if (today in transactionDates) today else today - 1
    var streak = 0
    var day = anchor
    while (day in transactionDates) {
        streak++
        day--
    }
    return streak
}

fun computeCategoryBreakdown(transactions: List<TransactionEntity>): List<CategoryBreakdown> {
    val now = YearMonth.now()
    val thisMonthExpenses = transactions.filter {
        it.type == TransactionType.EXPENSE.name && YearMonth.from(LocalDate.ofEpochDay(it.dateEpochDay)) == now
    }
    val total = thisMonthExpenses.sumOf { it.amount }
    if (total == 0L) return emptyList()
    return thisMonthExpenses.groupBy { TransactionCategory.fromName(it.category) }
        .map { (category, txs) ->
            val amount = txs.sumOf { it.amount }
            CategoryBreakdown(category, amount, amount.toFloat() / total.toFloat())
        }
        .sortedByDescending { it.amount }
}

fun computeMonthInsight(transactions: List<TransactionEntity>): MonthInsight {
    val now = YearMonth.now()
    val lastMonth = now.minusMonths(1)
    val thisMonthExpenses = transactions.filter {
        it.type == TransactionType.EXPENSE.name && YearMonth.from(LocalDate.ofEpochDay(it.dateEpochDay)) == now
    }
    val lastMonthExpenses = transactions.filter {
        it.type == TransactionType.EXPENSE.name && YearMonth.from(LocalDate.ofEpochDay(it.dateEpochDay)) == lastMonth
    }
    val totalThisMonth = thisMonthExpenses.sumOf { it.amount }
    val totalLastMonth = lastMonthExpenses.sumOf { it.amount }
    val topEntry = thisMonthExpenses.groupBy { TransactionCategory.fromName(it.category) }
        .mapValues { (_, txs) -> txs.sumOf { it.amount } }
        .maxByOrNull { it.value }
    return MonthInsight(
        topCategory = topEntry?.key,
        topCategoryAmount = topEntry?.value ?: 0L,
        totalThisMonth = totalThisMonth,
        totalLastMonth = totalLastMonth,
    )
}

fun computeBudgetProgress(transactions: List<TransactionEntity>, budgets: List<BudgetEntity>): List<BudgetProgress> {
    val now = YearMonth.now()
    val thisMonthExpensesByCategory = transactions
        .filter { it.type == TransactionType.EXPENSE.name && YearMonth.from(LocalDate.ofEpochDay(it.dateEpochDay)) == now }
        .groupBy { it.category }
        .mapValues { (_, txs) -> txs.sumOf { it.amount } }

    return budgets.map { budget ->
        BudgetProgress(
            category = TransactionCategory.fromName(budget.category),
            limit = budget.monthlyLimit,
            spent = thisMonthExpensesByCategory[budget.category] ?: 0L
        )
    }
}
