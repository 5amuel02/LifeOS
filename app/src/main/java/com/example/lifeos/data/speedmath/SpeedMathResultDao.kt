package com.example.lifeos.data.speedmath

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeedMathResultDao {
    @Query("SELECT * FROM speed_math_results ORDER BY score DESC LIMIT 1")
    fun getBestResult(): Flow<SpeedMathResultEntity?>

    @Insert
    suspend fun insert(result: SpeedMathResultEntity)
}
