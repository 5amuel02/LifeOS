package com.example.lifeos.data.savings

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SavingsCalculatorTest {

    @Test
    fun `returns 0 when the target is already reached`() {
        val months = estimateMonthsToTarget(
            currentAmount = 1_000,
            targetAmount = 500,
            monthlyContribution = 100,
            annualReturnRatePercent = 0.0,
        )
        assertEquals(0, months)
    }

    @Test
    fun `plain savings reaches the target through contributions`() {
        // 100 per month, no growth: 500 / 100 = 5 months.
        val months = estimateMonthsToTarget(
            currentAmount = 0,
            targetAmount = 500,
            monthlyContribution = 100,
            annualReturnRatePercent = 0.0,
        )
        assertEquals(5, months)
    }

    @Test
    fun `never reaches the target with no contribution and no growth`() {
        val months = estimateMonthsToTarget(
            currentAmount = 0,
            targetAmount = 500,
            monthlyContribution = 0,
            annualReturnRatePercent = 0.0,
        )
        assertNull(months)
    }

    @Test
    fun `compound growth alone can reach the target`() {
        // 1000 at 12%/yr (1%/month), no contributions: 1000 * 1.01^10 = 1104.6 >= 1100.
        val months = estimateMonthsToTarget(
            currentAmount = 1_000,
            targetAmount = 1_100,
            monthlyContribution = 0,
            annualReturnRatePercent = 12.0,
        )
        assertEquals(10, months)
    }

    @Test
    fun `toYearsMonths splits a month count into years and months`() {
        assertEquals(YearsMonths(years = 1, months = 2), 14.toYearsMonths())
        assertEquals(YearsMonths(years = 0, months = 0), 0.toYearsMonths())
        assertEquals(YearsMonths(years = 2, months = 0), 24.toYearsMonths())
    }
}
