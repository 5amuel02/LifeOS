package com.example.lifeos.data.savings

private const val MAX_MONTHS_CAP = 1200

/**
 * Estimates how many whole months are needed to reach [targetAmount], starting from
 * [currentAmount], depositing [monthlyContribution] at the end of every month, with the
 * balance compounding monthly at [annualReturnRatePercent] per year (0 for plain savings).
 *
 * Returns 0 if the target is already reached, null if it will never be reached
 * (no contribution and no growth, or growth too slow within [MAX_MONTHS_CAP] months).
 */
fun estimateMonthsToTarget(
    currentAmount: Long,
    targetAmount: Long,
    monthlyContribution: Long,
    annualReturnRatePercent: Double,
): Int? {
    if (currentAmount >= targetAmount) return 0

    val monthlyRate = annualReturnRatePercent / 100.0 / 12.0
    if (monthlyContribution <= 0 && monthlyRate <= 0.0) return null

    var balance = currentAmount.toDouble()
    for (month in 1..MAX_MONTHS_CAP) {
        balance = balance * (1 + monthlyRate) + monthlyContribution
        if (balance >= targetAmount) return month
    }
    return null
}

data class YearsMonths(val years: Int, val months: Int)

fun Int.toYearsMonths(): YearsMonths = YearsMonths(years = this / 12, months = this % 12)
