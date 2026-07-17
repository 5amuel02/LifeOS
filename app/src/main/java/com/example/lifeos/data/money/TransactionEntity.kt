package com.example.lifeos.data.money

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Long,
    val type: String,
    val category: String,
    val note: String,
    val dateEpochDay: Long,
    val createdAt: Long,
)
