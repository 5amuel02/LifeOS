package com.example.lifeos.data.money

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BudgetRepository @Inject constructor(private val dao: BudgetDao) {
    fun getAllBudgets(): Flow<List<BudgetEntity>> = dao.getAllBudgets()

    suspend fun setBudget(category: TransactionCategory, monthlyLimit: Long) {
        dao.upsert(BudgetEntity(category = category.name, monthlyLimit = monthlyLimit))
    }

    suspend fun removeBudget(category: TransactionCategory) {
        dao.deleteByCategory(category.name)
    }
}
