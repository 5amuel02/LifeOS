package com.example.lifeos.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.lifeos.core.strings.LocalStrings

sealed class Screen(val route: String, val icon: ImageVector) {
    data object Beranda : Screen("beranda", Icons.Filled.Home)
    data object Notes : Screen("notes", Icons.AutoMirrored.Filled.Notes)
    data object Habit : Screen("habit", Icons.Filled.CheckCircle)
    data object Pomodoro : Screen("pomodoro", Icons.Filled.Timer)
    data object Lainnya : Screen("lainnya", Icons.Filled.MoreHoriz)

    companion object {
        val bottomNavItems = listOf(Beranda, Notes, Habit, Pomodoro, Lainnya)
    }
}

val Screen.label: String
    @Composable get() {
        val strings = LocalStrings.current
        return when (this) {
            Screen.Beranda -> strings.nav.navBeranda
            Screen.Notes -> strings.nav.navNotes
            Screen.Habit -> strings.nav.navHabit
            Screen.Pomodoro -> strings.nav.navPomodoro
            Screen.Lainnya -> strings.nav.navLainnya
        }
    }
