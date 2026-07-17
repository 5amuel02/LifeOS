package com.example.lifeos.ui.screens.belajar.musik

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Piano
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.lifeos.core.strings.LocalStrings

enum class MusikGame(val route: String, val icon: ImageVector) {
    NOT_NADA("not_nada", Icons.Filled.MusicNote),
    RITME_TEMPO("ritme_tempo", Icons.Filled.Timer),
    ALAT_MUSIK("alat_musik", Icons.Filled.Piano),
    TEORI_MUSIK("teori_musik", Icons.Filled.GraphicEq);

    companion object {
        fun fromRoute(route: String?): MusikGame = entries.find { it.route == route } ?: NOT_NADA
    }
}

val MusikGame.label: String
    @Composable get() {
        val strings = LocalStrings.current
        return when (this) {
            MusikGame.NOT_NADA -> strings.musikGame.musikGameNotNada
            MusikGame.RITME_TEMPO -> strings.musikGame.musikGameRitme
            MusikGame.ALAT_MUSIK -> strings.musikGame.musikGameAlat
            MusikGame.TEORI_MUSIK -> strings.musikGame.musikGameTeori
        }
    }
