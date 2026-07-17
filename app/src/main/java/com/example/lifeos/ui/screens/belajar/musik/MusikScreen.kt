package com.example.lifeos.ui.screens.belajar.musik

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.common.GridMenuScreen

@Composable
fun MusikScreen(onBack: () -> Unit, onOpenGame: (MusikGame) -> Unit) {
    val strings = LocalStrings.current
    GridMenuScreen(
        title = strings.belajar.belajarSubjectMusik,
        items = MusikGame.entries.toList(),
        itemLabel = { it.label },
        itemIcon = { it.icon },
        onBack = onBack,
        onItemClick = onOpenGame
    )
}
