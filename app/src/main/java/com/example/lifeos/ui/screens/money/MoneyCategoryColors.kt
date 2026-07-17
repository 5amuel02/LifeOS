package com.example.lifeos.ui.screens.money

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.example.lifeos.data.money.TransactionCategory
import com.example.lifeos.data.money.TransactionType

// Validated categorical palette (dataviz skill reference palette): fixed hue order,
// worst adjacent CVD delta-E 24.2 (light) / 10.3 (dark) - never reassign by rank.
private val categoricalLight = listOf(
    Color(0xFF2A78D6), // blue
    Color(0xFF1BAF7A), // aqua
    Color(0xFFEDA100), // yellow
    Color(0xFF008300), // green
    Color(0xFF4A3AA7), // violet
    Color(0xFFE34948), // red
    Color(0xFFE87BA4), // magenta
)

private val categoricalDark = listOf(
    Color(0xFF3987E5),
    Color(0xFF199E70),
    Color(0xFFC98500),
    Color(0xFF008300),
    Color(0xFF9085E9),
    Color(0xFFE66767),
    Color(0xFFD55181),
)

@Composable
private fun isDarkSurface(): Boolean = MaterialTheme.colorScheme.surface.luminance() < 0.5f

@Composable
fun TransactionCategory.chartColor(): Color {
    val order = TransactionCategory.forType(TransactionType.EXPENSE)
    val index = order.indexOf(this).coerceAtLeast(0)
    val palette = if (isDarkSurface()) categoricalDark else categoricalLight
    return palette[index % palette.size]
}
