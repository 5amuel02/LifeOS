package com.example.lifeos.data.speedmath

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speed_math_results")
data class SpeedMathResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val score: Int,
    val averageAnswerTimeMillis: Long,
    val playedAt: Long,
)
