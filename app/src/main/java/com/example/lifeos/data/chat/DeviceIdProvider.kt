package com.example.lifeos.data.chat

import android.content.Context
import androidx.core.content.edit
import java.util.UUID

private const val PREFS_NAME = "lifeos_device"
private const val KEY_DEVICE_ID = "device_id"

/** A random per-install identifier sent to the AI backend so it can rate-limit per device. */
object DeviceIdProvider {
    fun getOrCreate(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.getString(KEY_DEVICE_ID, null)?.let { return it }
        val newId = UUID.randomUUID().toString()
        prefs.edit { putString(KEY_DEVICE_ID, newId) }
        return newId
    }
}
