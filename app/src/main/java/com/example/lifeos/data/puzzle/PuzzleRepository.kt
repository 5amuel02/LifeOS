package com.example.lifeos.data.puzzle

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PuzzleRepository @Inject constructor(private val dao: PuzzleResultDao) {
    fun getBestResult(): Flow<PuzzleResultEntity?> = dao.getBestResult()

    suspend fun getBestMovesOnce(): Int? = dao.getBestResult().first()?.moves

    suspend fun recordResult(moves: Int, timeMillis: Long) {
        dao.insert(
            PuzzleResultEntity(
                moves = moves,
                timeMillis = timeMillis,
                completedAt = System.currentTimeMillis()
            )
        )
    }
}
