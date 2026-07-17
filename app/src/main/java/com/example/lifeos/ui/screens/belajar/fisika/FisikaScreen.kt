package com.example.lifeos.ui.screens.belajar.fisika

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.common.GridMenuScreen

@Composable
fun FisikaScreen(onBack: () -> Unit, onOpenGame: (FisikaGame) -> Unit) {
    val strings = LocalStrings.current
    GridMenuScreen(
        title = strings.belajar.belajarSubjectFisika,
        items = FisikaGame.entries.toList(),
        itemLabel = { it.label },
        itemIcon = { it.icon },
        onBack = onBack,
        onItemClick = onOpenGame
    )
}
