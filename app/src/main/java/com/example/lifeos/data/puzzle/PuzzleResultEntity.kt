package com.example.lifeos.data.puzzle

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "puzzle_results")
data class PuzzleResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val moves: Int,
    val timeMillis: Long,
    val completedAt: Long,
)
