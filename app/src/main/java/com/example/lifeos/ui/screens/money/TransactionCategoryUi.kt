package com.example.lifeos.ui.screens.money

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.data.money.TransactionCategory

fun TransactionCategory.icon(): ImageVector = when (this) {
    TransactionCategory.FOOD -> Icons.Filled.Restaurant
    TransactionCategory.TRANSPORT -> Icons.Filled.DirectionsCar
    TransactionCategory.HOUSING -> Icons.Filled.Home
    TransactionCategory.ENTERTAINMENT -> Icons.Filled.Movie
    TransactionCategory.SHOPPING -> Icons.Filled.ShoppingBag
    TransactionCategory.HEALTH -> Icons.Filled.LocalHospital
    TransactionCategory.OTHER_EXPENSE -> Icons.Filled.MoreHoriz
    TransactionCategory.ALLOWANCE -> Icons.Filled.Wallet
    TransactionCategory.SALARY -> Icons.Filled.Payments
    TransactionCategory.BONUS -> Icons.Filled.CardGiftcard
    TransactionCategory.OTHER_INCOME -> Icons.Filled.MoreHoriz
}

fun TransactionCategory.label(strings: LifeOSStrings): String = when (this) {
    TransactionCategory.FOOD -> strings.money.moneyCategoryFood
    TransactionCategory.TRANSPORT -> strings.money.moneyCategoryTransport
    TransactionCategory.HOUSING -> strings.money.moneyCategoryHousing
    TransactionCategory.ENTERTAINMENT -> strings.money.moneyCategoryEntertainment
    TransactionCategory.SHOPPING -> strings.money.moneyCategoryShopping
    TransactionCategory.HEALTH -> strings.money.moneyCategoryHealth
    TransactionCategory.OTHER_EXPENSE -> strings.money.moneyCategoryOtherExpense
    TransactionCategory.ALLOWANCE -> strings.money.moneyCategoryAllowance
    TransactionCategory.SALARY -> strings.money.moneyCategorySalary
    TransactionCategory.BONUS -> strings.money.moneyCategoryBonus
    TransactionCategory.OTHER_INCOME -> strings.money.moneyCategoryOtherIncome
}
