package com.example.lifeos.data.savings

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goals")
data class SavingsGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val targetAmount: Long,
    val currentAmount: Long = 0,
    val monthlyContribution: Long,
    val annualReturnRatePercent: Double = 0.0,
    val createdAt: Long,
)
