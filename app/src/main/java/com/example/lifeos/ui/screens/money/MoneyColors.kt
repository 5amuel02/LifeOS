package com.example.lifeos.ui.screens.money

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/** Fixed green for income/positive amounts, independent of the app's theme accent color. */
@Composable
fun incomeColor(): Color {
    val isDarkSurface = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    return if (isDarkSurface) Color(0xFF4CAF50) else Color(0xFF0CA30C)
}
