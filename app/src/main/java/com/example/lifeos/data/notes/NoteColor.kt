package com.example.lifeos.data.notes

import android.graphics.Color as AndroidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb

val presetNoteColors = listOf(
    Color(0xFFF7CFCB),
    Color(0xFFF8E0B7),
    Color(0xFFF6F2AE),
    Color(0xFFCDE7C8),
    Color(0xFFC3DCF2),
    Color(0xFFDCCCEC),
)

/** Fixed dark/light text color chosen by luminance so it stays readable on any custom swatch. */
fun Color.contrastingOnColor(): Color =
    if (luminance() > 0.5f) Color(0xFF1C1B1F) else Color(0xFFF5F5F5)

fun Color.toNoteColorString(): String = String.format("#%08X", toArgb())

fun parseNoteColorString(stored: String): Color? {
    if (stored.isBlank() || stored == "DEFAULT") return null
    return runCatching { Color(AndroidColor.parseColor(stored)) }.getOrNull()
}
