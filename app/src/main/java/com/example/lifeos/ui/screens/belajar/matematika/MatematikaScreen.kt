package com.example.lifeos.ui.screens.belajar.matematika

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.common.GridMenuScreen

@Composable
fun MatematikaScreen(onBack: () -> Unit, onOpenGame: (MathGame) -> Unit) {
    val strings = LocalStrings.current
    GridMenuScreen(
        title = strings.belajar.belajarSubjectMatematika,
        items = MathGame.entries.toList(),
        itemLabel = { it.label },
        itemIcon = { it.icon },
        onBack = onBack,
        onItemClick = onOpenGame
    )
}
