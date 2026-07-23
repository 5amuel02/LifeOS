package com.example.lifeos.data.money

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class MoneyInsightsCalculationsTest {

    private fun expenseToday(amount: Long, category: TransactionCategory, daysAgo: Long = 0) =
        TransactionEntity(
            amount = amount,
            type = TransactionType.EXPENSE.name,
            category = category.name,
            note = "",
            dateEpochDay = LocalDate.now().toEpochDay() - daysAgo,
            createdAt = 0,
        )

    @Test
    fun `logging streak counts consecutive days ending today`() {
        val today = LocalDate.now().toEpochDay()
        assertEquals(3, calculateLoggingStreak(setOf(today, today - 1, today - 2)))
    }

    @Test
    fun `logging streak stops at the first gap`() {
        val today = LocalDate.now().toEpochDay()
        assertEquals(2, calculateLoggingStreak(setOf(today, today - 1, today - 3)))
    }

    @Test
    fun `logging streak counts from yesterday when today has no entry`() {
        val today = LocalDate.now().toEpochDay()
        assertEquals(2, calculateLoggingStreak(setOf(today - 1, today - 2)))
    }

    @Test
    fun `logging streak is zero with no recent entries`() {
        assertEquals(0, calculateLoggingStreak(emptySet()))
    }

    @Test
    fun `budget progress is spent divided by limit`() {
        val progress = BudgetProgress(TransactionCategory.FOOD, limit = 200, spent = 50).progress
        assertEquals(0.25f, progress, 0.0001f)
    }

    @Test
    fun `budget progress is zero when the limit is zero`() {
        val progress = BudgetProgress(TransactionCategory.FOOD, limit = 0, spent = 100).progress
        assertEquals(0f, progress, 0.0001f)
    }

    @Test
    fun `percent change is null when last month had no spending`() {
        val insight = MonthInsight(topCategory = null, topCategoryAmount = 0, totalThisMonth = 100, totalLastMonth = 0)
        assertNull(insight.percentChange)
    }

    @Test
    fun `percent change compares this month against last`() {
        val insight = MonthInsight(topCategory = null, topCategoryAmount = 0, totalThisMonth = 150, totalLastMonth = 100)
        assertEquals(50f, insight.percentChange!!, 0.0001f)
    }

    @Test
    fun `budget progress aggregates this month expenses per category`() {
        val transactions = listOf(
            expenseToday(30_000, TransactionCategory.FOOD),
            expenseToday(20_000, TransactionCategory.FOOD),
            expenseToday(10_000, TransactionCategory.TRANSPORT),
        )
        val budgets = listOf(
            BudgetEntity(TransactionCategory.FOOD.name, monthlyLimit = 100_000),
            BudgetEntity(TransactionCategory.TRANSPORT.name, monthlyLimit = 50_000),
        )
        val byCategory = computeBudgetProgress(transactions, budgets).associateBy { it.category }
        assertEquals(50_000, byCategory.getValue(TransactionCategory.FOOD).spent)
        assertEquals(10_000, byCategory.getValue(TransactionCategory.TRANSPORT).spent)
    }
}
