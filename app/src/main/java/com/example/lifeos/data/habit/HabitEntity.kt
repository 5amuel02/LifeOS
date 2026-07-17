package com.example.lifeos.data.habit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val createdAt: Long,
    /** Reminder time as minutes since midnight (0..1439), or null when no reminder is set. */
    val reminderMinuteOfDay: Int? = null,
)
