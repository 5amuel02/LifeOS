package com.example.lifeos.ui.screens.belajar.fisika

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.lifeos.core.strings.LocalStrings

enum class FisikaGame(val route: String, val icon: ImageVector) {
    BESARAN("besaran", Icons.Filled.Straighten),
    GERAK("gerak", Icons.Filled.Speed),
    ENERGI("energi", Icons.Filled.Whatshot),
    LISTRIK("listrik", Icons.Filled.FlashOn);

    companion object {
        fun fromRoute(route: String?): FisikaGame = entries.find { it.route == route } ?: BESARAN
    }
}

val FisikaGame.label: String
    @Composable get() {
        val strings = LocalStrings.current
        return when (this) {
            FisikaGame.BESARAN -> strings.fisikaGame.fisikaGameBesaran
            FisikaGame.GERAK -> strings.fisikaGame.fisikaGameGerak
            FisikaGame.ENERGI -> strings.fisikaGame.fisikaGameEnergi
            FisikaGame.LISTRIK -> strings.fisikaGame.fisikaGameListrik
        }
    }
