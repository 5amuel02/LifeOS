package com.example.lifeos.ui.screens.belajar.kimia

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.common.GridMenuScreen

@Composable
fun KimiaScreen(onBack: () -> Unit, onOpenGame: (KimiaGame) -> Unit) {
    val strings = LocalStrings.current
    GridMenuScreen(
        title = strings.belajar.belajarSubjectKimia,
        items = KimiaGame.entries.toList(),
        itemLabel = { it.label },
        itemIcon = { it.icon },
        onBack = onBack,
        onItemClick = onOpenGame
    )
}
