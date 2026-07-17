package com.example.lifeos.data.savings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsDepositDao {
    @Query("SELECT * FROM savings_deposits WHERE goalId = :goalId ORDER BY dateEpochDay DESC, createdAt DESC")
    fun getDepositsForGoal(goalId: Long): Flow<List<SavingsDepositEntity>>

    @Insert
    suspend fun insert(deposit: SavingsDepositEntity): Long

    @Query("DELETE FROM savings_deposits WHERE id = :id")
    suspend fun deleteById(id: Long)
}
