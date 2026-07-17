package com.example.lifeos.ui.screens.belajar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.lifeos.core.strings.LocalStrings

enum class BelajarSubject(val route: String, val icon: ImageVector) {
    MATEMATIKA("matematika", Icons.Filled.Calculate),
    BAHASA_INGGRIS("bahasa_inggris", Icons.Filled.Translate),
    FISIKA("fisika", Icons.Filled.Bolt),
    KIMIA("kimia", Icons.Filled.Science),
    MUSIK("musik", Icons.Filled.MusicNote),
    SENI("seni", Icons.Filled.Palette);

    companion object {
        fun fromRoute(route: String?): BelajarSubject = entries.find { it.route == route } ?: MATEMATIKA
    }
}

val BelajarSubject.label: String
    @Composable get() {
        val strings = LocalStrings.current
        return when (this) {
            BelajarSubject.MATEMATIKA -> strings.belajar.belajarSubjectMatematika
            BelajarSubject.BAHASA_INGGRIS -> strings.belajar.belajarSubjectBahasaInggris
            BelajarSubject.FISIKA -> strings.belajar.belajarSubjectFisika
            BelajarSubject.KIMIA -> strings.belajar.belajarSubjectKimia
            BelajarSubject.MUSIK -> strings.belajar.belajarSubjectMusik
            BelajarSubject.SENI -> strings.belajar.belajarSubjectSeni
        }
    }
