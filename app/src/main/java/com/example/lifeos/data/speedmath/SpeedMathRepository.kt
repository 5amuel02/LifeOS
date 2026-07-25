package com.example.lifeos.data.speedmath

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SpeedMathRepository @Inject constructor(private val dao: SpeedMathResultDao) {
    fun getBestResult(): Flow<SpeedMathResultEntity?> = dao.getBestResult()

    suspend fun getBestScoreOnce(): Int = dao.getBestResult().first()?.score ?: 0

    suspend fun recordResult(score: Int, averageAnswerTimeMillis: Long) {
        dao.insert(
            SpeedMathResultEntity(
                score = score,
                averageAnswerTimeMillis = averageAnswerTimeMillis,
                playedAt = System.currentTimeMillis()
            )
        )
    }
}
