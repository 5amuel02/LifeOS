package com.example.lifeos.data.savings

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class SavingsRepository(
    private val goalDao: SavingsGoalDao,
    private val depositDao: SavingsDepositDao,
) {
    fun getAllGoals(): Flow<List<SavingsGoalEntity>> = goalDao.getAllGoals()

    fun getGoalById(id: Long): Flow<SavingsGoalEntity?> = goalDao.getGoalById(id)

    fun getDepositsForGoal(goalId: Long): Flow<List<SavingsDepositEntity>> = depositDao.getDepositsForGoal(goalId)

    suspend fun addGoal(
        name: String,
        targetAmount: Long,
        monthlyContribution: Long,
        annualReturnRatePercent: Double,
    ) {
        goalDao.insert(
            SavingsGoalEntity(
                name = name,
                targetAmount = targetAmount,
                monthlyContribution = monthlyContribution,
                annualReturnRatePercent = annualReturnRatePercent,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun addDeposit(goalId: Long, amount: Long, date: LocalDate) {
        depositDao.insert(
            SavingsDepositEntity(
                goalId = goalId,
                amount = amount,
                dateEpochDay = date.toEpochDay(),
                createdAt = System.currentTimeMillis()
            )
        )
        goalDao.addToCurrentAmount(goalId, amount)
    }

    suspend fun deleteGoal(id: Long) = goalDao.deleteById(id)

    suspend fun deleteDeposit(deposit: SavingsDepositEntity) {
        depositDao.deleteById(deposit.id)
        goalDao.addToCurrentAmount(deposit.goalId, -deposit.amount)
    }
}
