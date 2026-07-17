package com.example.lifeos.data.money

import androidx.room.Entity

@Entity(tableName = "budgets", primaryKeys = ["category"])
data class BudgetEntity(
    val category: String,
    val monthlyLimit: Long,
)
