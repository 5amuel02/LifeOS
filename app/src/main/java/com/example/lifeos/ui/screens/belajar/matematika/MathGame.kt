package com.example.lifeos.ui.screens.belajar.matematika

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Speed
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.lifeos.core.strings.LocalStrings

enum class MathGame(val route: String, val icon: ImageVector) {
    HITUNG_CEPAT("hitung_cepat", Icons.Filled.Speed),
    HOTS("hots", Icons.Filled.Psychology),
    LOGIKA("logika", Icons.Filled.Lightbulb),
    TEKA_TEKI("teka_teki", Icons.Filled.Quiz),
    PUZZLE("puzzle", Icons.Filled.Extension);

    companion object {
        fun fromRoute(route: String?): MathGame = entries.find { it.route == route } ?: HITUNG_CEPAT
    }
}

val MathGame.label: String
    @Composable get() {
        val strings = LocalStrings.current
        return when (this) {
            MathGame.HITUNG_CEPAT -> strings.mathGame.mathGameHitungCepat
            MathGame.HOTS -> strings.mathGame.mathGameHots
            MathGame.LOGIKA -> strings.mathGame.mathGameLogika
            MathGame.TEKA_TEKI -> strings.mathGame.mathGameTekaTeki
            MathGame.PUZZLE -> strings.mathGame.mathGamePuzzle
        }
    }
