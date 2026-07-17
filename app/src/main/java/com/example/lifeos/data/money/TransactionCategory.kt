package com.example.lifeos.data.money

enum class TransactionCategory(val type: TransactionType) {
    FOOD(TransactionType.EXPENSE),
    TRANSPORT(TransactionType.EXPENSE),
    HOUSING(TransactionType.EXPENSE),
    ENTERTAINMENT(TransactionType.EXPENSE),
    SHOPPING(TransactionType.EXPENSE),
    HEALTH(TransactionType.EXPENSE),
    OTHER_EXPENSE(TransactionType.EXPENSE),
    ALLOWANCE(TransactionType.INCOME),
    SALARY(TransactionType.INCOME),
    BONUS(TransactionType.INCOME),
    OTHER_INCOME(TransactionType.INCOME);

    companion object {
        fun fromName(name: String): TransactionCategory = entries.find { it.name == name } ?: OTHER_EXPENSE

        fun forType(type: TransactionType): List<TransactionCategory> = entries.filter { it.type == type }
    }
}
