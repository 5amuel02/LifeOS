package com.example.lifeos.ui.screens.belajar.bahasainggris

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Rule
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.lifeos.core.strings.LocalStrings

enum class BahasaInggrisGame(val route: String, val icon: ImageVector) {
    KOSAKATA("kosakata", Icons.AutoMirrored.Filled.MenuBook),
    TATA_BAHASA("tata_bahasa", Icons.AutoMirrored.Filled.Rule),
    SUSUN_KALIMAT("susun_kalimat", Icons.Filled.Reorder),
    LISTENING("listening", Icons.Filled.Headphones);

    companion object {
        fun fromRoute(route: String?): BahasaInggrisGame = entries.find { it.route == route } ?: KOSAKATA
    }
}

val BahasaInggrisGame.label: String
    @Composable get() {
        val strings = LocalStrings.current
        return when (this) {
            BahasaInggrisGame.KOSAKATA -> strings.bahasaInggrisGame.bahasaInggrisGameKosakata
            BahasaInggrisGame.TATA_BAHASA -> strings.bahasaInggrisGame.bahasaInggrisGameTataBahasa
            BahasaInggrisGame.SUSUN_KALIMAT -> strings.bahasaInggrisGame.bahasaInggrisGameSusunKalimat
            BahasaInggrisGame.LISTENING -> strings.bahasaInggrisGame.bahasaInggrisGameListening
        }
    }
