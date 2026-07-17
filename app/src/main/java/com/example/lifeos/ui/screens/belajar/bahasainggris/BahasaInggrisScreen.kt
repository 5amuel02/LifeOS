package com.example.lifeos.ui.screens.belajar.bahasainggris

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.common.GridMenuScreen

@Composable
fun BahasaInggrisScreen(onBack: () -> Unit, onOpenGame: (BahasaInggrisGame) -> Unit) {
    val strings = LocalStrings.current
    GridMenuScreen(
        title = strings.belajar.belajarSubjectBahasaInggris,
        items = BahasaInggrisGame.entries.toList(),
        itemLabel = { it.label },
        itemIcon = { it.icon },
        onBack = onBack,
        onItemClick = onOpenGame
    )
}
