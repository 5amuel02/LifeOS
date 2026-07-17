package com.example.lifeos.data.money

enum class TransactionType {
    INCOME,
    EXPENSE;

    companion object {
        fun fromName(name: String): TransactionType = entries.find { it.name == name } ?: EXPENSE
    }
}
