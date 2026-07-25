package com.example.lifeos.ui.screens.savings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.savings.SavingsDepositEntity
import com.example.lifeos.data.savings.SavingsGoalEntity
import com.example.lifeos.data.savings.SavingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SavingsGoalDetailViewModel @Inject constructor(
    private val repository: SavingsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    // "goalId" matches the required nav argument key declared for this route in LifeOSNavHost.
    private val goalId: Long = savedStateHandle.get<Long>("goalId") ?: 0L

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
