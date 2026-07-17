package com.example.lifeos.ui.screens.savings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.LifeOSDatabase
import com.example.lifeos.data.savings.SavingsDepositEntity
import com.example.lifeos.data.savings.SavingsGoalEntity
import com.example.lifeos.data.savings.SavingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class SavingsGoalDetailViewModel(application: Application, private val goalId: Long) : AndroidViewModel(application) {
    private val database = LifeOSDatabase.getInstance(application)
    private val repository = SavingsRepository(database.savingsGoalDao(), database.savingsDepositDao())

    val goal: StateFlow<SavingsGoalEntity?> = repository.getGoalById(goalId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val deposits: StateFlow<List<SavingsDepositEntity>> = repository.getDepositsForGoal(goalId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addDeposit(amount: Long, date: LocalDate) {
        if (amount <= 0) return
        viewModelScope.launch { repository.addDeposit(goalId, amount, date) }
    }

    fun deleteDeposit(deposit: SavingsDepositEntity) {
        viewModelScope.launch { repository.deleteDeposit(deposit) }
    }

    fun deleteGoal(onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deleteGoal(goalId)
            onDeleted()
        }
    }
}

class SavingsGoalDetailViewModelFactory(
    private val application: Application,
    private val goalId: Long,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SavingsGoalDetailViewModel(application, goalId) as T
    }
}
