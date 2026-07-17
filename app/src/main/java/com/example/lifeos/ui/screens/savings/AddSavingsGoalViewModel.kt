package com.example.lifeos.ui.screens.savings

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.LifeOSDatabase
import com.example.lifeos.data.savings.SavingsRepository
import com.example.lifeos.data.savings.estimateMonthsToTarget
import kotlinx.coroutines.launch

class AddSavingsGoalViewModel(application: Application) : AndroidViewModel(application) {
    private val database = LifeOSDatabase.getInstance(application)
    private val repository = SavingsRepository(database.savingsGoalDao(), database.savingsDepositDao())

    var name by mutableStateOf("")
        private set
    var targetAmountText by mutableStateOf("")
        private set
    var monthlyContributionText by mutableStateOf("")
        private set
    var annualReturnRateText by mutableStateOf("")
        private set

    val targetAmount: Long get() = targetAmountText.toLongOrNull() ?: 0L
    val monthlyContribution: Long get() = monthlyContributionText.toLongOrNull() ?: 0L
    val annualReturnRate: Double get() = annualReturnRateText.toDoubleOrNull() ?: 0.0
    val canSave: Boolean get() = name.isNotBlank() && targetAmount > 0

    val estimatedMonths: Int?
        get() = estimateMonthsToTarget(
            currentAmount = 0,
            targetAmount = targetAmount,
            monthlyContribution = monthlyContribution,
            annualReturnRatePercent = annualReturnRate
        )

    fun onNameChange(value: String) {
        name = value
    }

    fun onTargetAmountChange(value: String) {
        targetAmountText = value.filter { it.isDigit() }
    }

    fun onMonthlyContributionChange(value: String) {
        monthlyContributionText = value.filter { it.isDigit() }
    }

    fun onAnnualReturnRateChange(value: String) {
        annualReturnRateText = value.filter { it.isDigit() || it == '.' }
    }

    fun save(onSaved: () -> Unit) {
        if (!canSave) return
        viewModelScope.launch {
            repository.addGoal(name.trim(), targetAmount, monthlyContribution, annualReturnRate)
            onSaved()
        }
    }
}
