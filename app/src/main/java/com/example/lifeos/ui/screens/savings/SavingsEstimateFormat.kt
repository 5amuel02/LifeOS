package com.example.lifeos.ui.screens.savings

import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.data.savings.toYearsMonths

fun formatEstimate(months: Int?, strings: LifeOSStrings): String {
    if (months == null) return strings.savings.savingsEstimateNeverTemplate
    if (months == 0) return strings.savings.savingsEstimateAchieved
    val yearsMonths = months.toYearsMonths()
    return if (yearsMonths.years > 0) {
        strings.savings.savingsEstimateYearsMonthsTemplate.format(yearsMonths.years, yearsMonths.months)
    } else {
        strings.savings.savingsEstimateMonthsTemplate.format(yearsMonths.months)
    }
}
