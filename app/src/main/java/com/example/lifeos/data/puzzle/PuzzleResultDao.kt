package com.example.lifeos.data.puzzle

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PuzzleResultDao {
    @Query("SELECT * FROM puzzle_results ORDER BY moves ASC LIMIT 1")
    fun getBestResult(): Flow<PuzzleResultEntity?>

    @Insert
    suspend fun insert(result: PuzzleResultEntity)
}
