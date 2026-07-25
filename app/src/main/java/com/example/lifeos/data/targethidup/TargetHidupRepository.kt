package com.example.lifeos.data.targethidup

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TargetHidupRepository @Inject constructor(private val dao: TargetHidupDao) {
    fun getAll(): Flow<List<TargetHidupEntity>> = dao.getAll()

    suspend fun addGoal(title: String, targetDateEpochDay: Long?): Long {
        return dao.insert(
            TargetHidupEntity(
                title = title,
                targetDateEpochDay = targetDateEpochDay,
                createdAt = System.currentTimeMillis(),
            )
        )
    }

    suspend fun updateProgress(id: Long, progressPercent: Int) =
        dao.updateProgress(id, progressPercent.coerceIn(0, 100))

    suspend fun deleteGoal(id: Long) = dao.deleteById(id)
}
