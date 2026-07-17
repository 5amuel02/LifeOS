package com.example.lifeos.ui.screens.belajar.kimia

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.lifeos.core.strings.LocalStrings

enum class KimiaGame(val route: String, val icon: ImageVector) {
    ATOM("atom", Icons.Filled.Grain),
    IKATAN("ikatan", Icons.Filled.Link),
    ASAM_BASA("asam_basa", Icons.Filled.Opacity),
    WUJUD("wujud", Icons.Filled.AcUnit);

    companion object {
        fun fromRoute(route: String?): KimiaGame = entries.find { it.route == route } ?: ATOM
    }
}

val KimiaGame.label: String
    @Composable get() {
        val strings = LocalStrings.current
        return when (this) {
            KimiaGame.ATOM -> strings.kimiaGame.kimiaGameAtom
            KimiaGame.IKATAN -> strings.kimiaGame.kimiaGameIkatan
            KimiaGame.ASAM_BASA -> strings.kimiaGame.kimiaGameAsamBasa
            KimiaGame.WUJUD -> strings.kimiaGame.kimiaGameWujud
        }
    }
