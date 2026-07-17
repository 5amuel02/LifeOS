package com.example.lifeos.data.money

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TransactionRepository(private val dao: TransactionDao) {
    fun getAllTransactions(): Flow<List<TransactionEntity>> = dao.getAllTransactions()

    suspend fun addTransaction(
        amount: Long,
        type: TransactionType,
        category: TransactionCategory,
        note: String,
        date: LocalDate,
    ) {
        dao.insert(
            TransactionEntity(
                amount = amount,
                type = type.name,
                category = category.name,
                note = note,
                dateEpochDay = date.toEpochDay(),
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteTransaction(id: Long) = dao.deleteById(id)
}
