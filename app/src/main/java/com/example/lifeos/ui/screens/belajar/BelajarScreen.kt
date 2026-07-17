package com.example.lifeos.ui.screens.belajar

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.common.GridMenuScreen

@Composable
fun BelajarScreen(onBack: () -> Unit, onOpenSubject: (BelajarSubject) -> Unit) {
    val strings = LocalStrings.current
    GridMenuScreen(
        title = strings.belajar.belajarTitle,
        items = BelajarSubject.entries.toList(),
        itemLabel = { it.label },
        itemIcon = { it.icon },
        onBack = onBack,
        onItemClick = onOpenSubject
    )
}
