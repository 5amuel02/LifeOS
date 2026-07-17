package com.example.lifeos.ui.screens.belajar.seni

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.lifeos.core.strings.LocalStrings

enum class SeniGame(val route: String, val icon: ImageVector) {
    WARNA("warna", Icons.Filled.ColorLens),
    UNSUR("unsur", Icons.Filled.Category),
    TEKNIK("teknik", Icons.Filled.Brush),
    ALIRAN("aliran", Icons.Filled.Image);

    companion object {
        fun fromRoute(route: String?): SeniGame = entries.find { it.route == route } ?: WARNA
    }
}

val SeniGame.label: String
    @Composable get() {
        val strings = LocalStrings.current
        return when (this) {
            SeniGame.WARNA -> strings.seniGame.seniGameWarna
            SeniGame.UNSUR -> strings.seniGame.seniGameUnsur
            SeniGame.TEKNIK -> strings.seniGame.seniGameTeknik
            SeniGame.ALIRAN -> strings.seniGame.seniGameAliran
        }
    }
