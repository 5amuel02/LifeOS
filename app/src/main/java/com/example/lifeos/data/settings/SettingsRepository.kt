package com.example.lifeos.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lifeos.core.strings.AppLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

enum class ThemeMode { LIGHT, DARK, SYSTEM }

const val DEFAULT_FOCUS_MINUTES = 25
const val DEFAULT_SHORT_BREAK_MINUTES = 5
const val DEFAULT_LONG_BREAK_MINUTES = 15

data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val language: AppLanguage = AppLanguage.INDONESIAN,
    val focusMinutes: Int = DEFAULT_FOCUS_MINUTES,
    val shortBreakMinutes: Int = DEFAULT_SHORT_BREAK_MINUTES,
    val longBreakMinutes: Int = DEFAULT_LONG_BREAK_MINUTES,
)

private val Context.settingsDataStore by preferencesDataStore(name = "lifeos_settings")

class SettingsRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val FOCUS_MINUTES = intPreferencesKey("focus_minutes")
        val SHORT_BREAK_MINUTES = intPreferencesKey("short_break_minutes")
        val LONG_BREAK_MINUTES = intPreferencesKey("long_break_minutes")
    }

    val settings: Flow<AppSettings> = context.settingsDataStore.data.map { prefs ->
        AppSettings(
            themeMode = prefs[Keys.THEME_MODE]?.let { raw ->
                runCatching { ThemeMode.valueOf(raw) }.getOrNull()
            } ?: ThemeMode.SYSTEM,
            language = prefs[Keys.LANGUAGE]?.let { raw ->
                runCatching { AppLanguage.valueOf(raw) }.getOrNull()
            } ?: AppLanguage.INDONESIAN,
            focusMinutes = prefs[Keys.FOCUS_MINUTES] ?: DEFAULT_FOCUS_MINUTES,
            shortBreakMinutes = prefs[Keys.SHORT_BREAK_MINUTES] ?: DEFAULT_SHORT_BREAK_MINUTES,
            longBreakMinutes = prefs[Keys.LONG_BREAK_MINUTES] ?: DEFAULT_LONG_BREAK_MINUTES,
        )
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.settingsDataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.settingsDataStore.edit { it[Keys.LANGUAGE] = language.name }
    }

    suspend fun setFocusMinutes(minutes: Int) {
        context.settingsDataStore.edit { it[Keys.FOCUS_MINUTES] = minutes }
    }

    suspend fun setShortBreakMinutes(minutes: Int) {
        context.settingsDataStore.edit { it[Keys.SHORT_BREAK_MINUTES] = minutes }
    }

    suspend fun setLongBreakMinutes(minutes: Int) {
        context.settingsDataStore.edit { it[Keys.LONG_BREAK_MINUTES] = minutes }
    }
}
