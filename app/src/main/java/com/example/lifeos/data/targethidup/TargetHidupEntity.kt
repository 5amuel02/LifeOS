package com.example.lifeos.data.targethidup

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "target_hidup")
data class TargetHidupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    /** Optional deadline; null means the goal has no target date. */
    val targetDateEpochDay: Long? = null,
    val progressPercent: Int = 0,
    val createdAt: Long,
)
