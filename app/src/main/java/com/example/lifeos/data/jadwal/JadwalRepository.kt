package com.example.lifeos.data.jadwal

import kotlinx.coroutines.flow.Flow

class JadwalRepository(private val jadwalDao: JadwalDao) {
    fun getAll(): Flow<List<JadwalEntity>> = jadwalDao.getAll()

    suspend fun getAllOnce(): List<JadwalEntity> = jadwalDao.getAllOnce()

    suspend fun getByIdOnce(id: Long): JadwalEntity? = jadwalDao.getByIdOnce(id)

    suspend fun addItem(title: String, dateEpochDay: Long, minuteOfDay: Int?): Long {
        return jadwalDao.insert(
            JadwalEntity(
                title = title,
                dateEpochDay = dateEpochDay,
                minuteOfDay = minuteOfDay,
                createdAt = System.currentTimeMillis(),
            )
        )
    }

    suspend fun setCompleted(id: Long, isCompleted: Boolean) = jadwalDao.updateCompleted(id, isCompleted)

    suspend fun deleteItem(id: Long) = jadwalDao.deleteById(id)
}
