package com.example.lifeos.ui.screens.money

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.money.BudgetProgress
import com.example.lifeos.data.money.BudgetRepository
import com.example.lifeos.data.money.CategoryBreakdown
import com.example.lifeos.data.money.MonthInsight
import com.example.lifeos.data.money.TransactionCategory
import com.example.lifeos.data.money.TransactionRepository
import com.example.lifeos.data.money.calculateLoggingStreak
import com.example.lifeos.data.money.computeBudgetProgress
import com.example.lifeos.data.money.computeCategoryBreakdown
import com.example.lifeos.data.money.computeMonthInsight
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoneyInsightsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository,
) : ViewModel() {

    private val transactions = transactionRepository.getAllTransactions()

    val streak: StateFlow<Int> = transactions
        .map { list -> calculateLoggingStreak(list.map { it.dateEpochDay }.toSet()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val monthInsight: StateFlow<MonthInsight> = transactions
        .map { computeMonthInsight(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MonthInsight(null, 0, 0, 0))

    val categoryBreakdown: StateFlow<List<CategoryBreakdown>> = transactions
        .map { computeCategoryBreakdown(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val budgetProgress: StateFlow<List<BudgetProgress>> = combine(
        transactions,
        budgetRepository.getAllBudgets()
    ) { txs, budgets -> computeBudgetProgress(txs, budgets) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setBudget(category: TransactionCategory, monthlyLimit: Long) {
        viewModelScope.launch { budgetRepository.setBudget(category, monthlyLimit) }
    }

    fun removeBudget(category: TransactionCategory) {
        viewModelScope.launch { budgetRepository.removeBudget(category) }
    }
}
