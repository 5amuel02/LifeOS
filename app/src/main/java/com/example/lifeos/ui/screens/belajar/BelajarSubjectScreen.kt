package com.example.lifeos.ui.screens.belajar

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.common.PlaceholderTopBarScreen

@Composable
fun BelajarSubjectScreen(subject: BelajarSubject, onBack: () -> Unit) {
    val strings = LocalStrings.current
    PlaceholderTopBarScreen(
        title = subject.label,
        subtitle = strings.lainnya.comingSoon,
        icon = subject.icon,
        onBack = onBack
    )
}
