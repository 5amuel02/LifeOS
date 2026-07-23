package com.example.lifeos.ui.screens.money

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.money.TransactionEntity
import com.example.lifeos.data.money.TransactionRepository
import com.example.lifeos.data.money.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MoneySummary(val totalIncome: Long, val totalExpense: Long) {
    val balance: Long get() = totalIncome - totalExpense
}

@HiltViewModel
class MoneyManagerViewModel @Inject constructor(
    private val repository: TransactionRepository,
) : ViewModel() {

    val transactions: StateFlow<List<TransactionEntity>> = repository.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val summary: StateFlow<MoneySummary> = transactions
        .map { list ->
            val income = list.filter { it.type == TransactionType.INCOME.name }.sumOf { it.amount }
            val expense = list.filter { it.type == TransactionType.EXPENSE.name }.sumOf { it.amount }
            MoneySummary(totalIncome = income, totalExpense = expense)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MoneySummary(0, 0))

    fun deleteTransaction(id: Long) {
        viewModelScope.launch { repository.deleteTransaction(id) }
    }
}
