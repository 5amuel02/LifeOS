package com.example.lifeos.data.jadwal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jadwal")
data class JadwalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val dateEpochDay: Long,
    /** Minutes since midnight (0..1439), or null when the item has no specific time. */
    val minuteOfDay: Int? = null,
    val isCompleted: Boolean = false,
    val createdAt: Long,
)
