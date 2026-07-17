package com.example.lifeos.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.core.strings.AppLanguage
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.data.settings.ThemeMode
import kotlinx.coroutines.launch

private const val FOCUS_MIN_MINUTES = 5
private const val FOCUS_MAX_MINUTES = 120
private const val FOCUS_STEP_MINUTES = 5
private const val SHORT_BREAK_MIN_MINUTES = 1
private const val SHORT_BREAK_MAX_MINUTES = 30
private const val SHORT_BREAK_STEP_MINUTES = 1
private const val LONG_BREAK_MIN_MINUTES = 5
private const val LONG_BREAK_MAX_MINUTES = 60
private const val LONG_BREAK_STEP_MINUTES = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val viewModel: SettingsViewModel = viewModel()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val strings = LocalStrings.current
    var showClearConfirm by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.settings.settingsTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingsSection(title = strings.settings.settingsThemeSection) {
                SettingsRadioRow(
                    label = strings.settings.settingsThemeLight,
                    selected = settings.themeMode == ThemeMode.LIGHT,
                    onClick = { viewModel.setThemeMode(ThemeMode.LIGHT) }
                )
                SettingsRadioRow(
                    label = strings.settings.settingsThemeDark,
                    selected = settings.themeMode == ThemeMode.DARK,
                    onClick = { viewModel.setThemeMode(ThemeMode.DARK) }
                )
                SettingsRadioRow(
                    label = strings.settings.settingsThemeSystem,
                    selected = settings.themeMode == ThemeMode.SYSTEM,
                    onClick = { viewModel.setThemeMode(ThemeMode.SYSTEM) }
                )
            }

            SettingsSection(title = strings.settings.settingsLanguageSection) {
                SettingsRadioRow(
                    label = strings.settings.settingsLanguageIndonesian,
                    selected = settings.language == AppLanguage.INDONESIAN,
                    onClick = { viewModel.setLanguage(AppLanguage.INDONESIAN) }
                )
                SettingsRadioRow(
                    label = strings.settings.settingsLanguageEnglish,
                    selected = settings.language == AppLanguage.ENGLISH,
                    onClick = { viewModel.setLanguage(AppLanguage.ENGLISH) }
                )
            }

            SettingsSection(title = strings.settings.settingsPomodoroSection) {
                MinutesStepperRow(
                    label = strings.settings.settingsPomodoroFocusLabel,
                    minutesTemplate = strings.settings.settingsMinutesTemplate,
                    value = settings.focusMinutes,
                    minValue = FOCUS_MIN_MINUTES,
                    maxValue = FOCUS_MAX_MINUTES,
                    step = FOCUS_STEP_MINUTES,
                    decreaseContentDescription = strings.settings.settingsDecreaseContentDesc,
                    increaseContentDescription = strings.settings.settingsIncreaseContentDesc,
                    onValueChange = viewModel::setFocusMinutes
                )
                MinutesStepperRow(
                    label = strings.settings.settingsPomodoroShortBreakLabel,
                    minutesTemplate = strings.settings.settingsMinutesTemplate,
                    value = settings.shortBreakMinutes,
                    minValue = SHORT_BREAK_MIN_MINUTES,
                    maxValue = SHORT_BREAK_MAX_MINUTES,
                    step = SHORT_BREAK_STEP_MINUTES,
                    decreaseContentDescription = strings.settings.settingsDecreaseContentDesc,
                    increaseContentDescription = strings.settings.settingsIncreaseContentDesc,
                    onValueChange = viewModel::setShortBreakMinutes
                )
                MinutesStepperRow(
                    label = strings.settings.settingsPomodoroLongBreakLabel,
                    minutesTemplate = strings.settings.settingsMinutesTemplate,
                    value = settings.longBreakMinutes,
                    minValue = LONG_BREAK_MIN_MINUTES,
                    maxValue = LONG_BREAK_MAX_MINUTES,
                    step = LONG_BREAK_STEP_MINUTES,
                    decreaseContentDescription = strings.settings.settingsDecreaseContentDesc,
                    increaseContentDescription = strings.settings.settingsIncreaseContentDesc,
                    onValueChange = viewModel::setLongBreakMinutes
                )
            }

            SettingsSection(title = strings.settings.settingsAboutSection) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "LifeOS", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = strings.settings.settingsAboutVersion,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = strings.settings.settingsAboutDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = strings.settings.settingsAboutTagline,
                            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Verified,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Column {
                                Text(
                                    text = strings.settings.settingsAboutCreatorLabel,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = strings.settings.settingsAboutCreatorName,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            SettingsSection(title = strings.settings.settingsDataSection) {
                Button(
                    onClick = { showClearConfirm = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.DeleteForever, contentDescription = null)
                    Text(text = strings.settings.settingsClearDataButton, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text(strings.settings.settingsClearDataConfirmTitle) },
            text = { Text(strings.settings.settingsClearDataConfirmText) },
            confirmButton = {
                TextButton(onClick = {
                    showClearConfirm = false
                    viewModel.clearAllData {
                        scope.launch { snackbarHostState.showSnackbar(strings.settings.settingsClearDataDone) }
                    }
                }) { Text(strings.settings.settingsClearDataConfirm) }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) { Text(strings.settings.settingsClearDataCancel) }
            }
        )
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        content()
    }
}

@Composable
private fun SettingsRadioRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun MinutesStepperRow(
    label: String,
    minutesTemplate: String,
    value: Int,
    minValue: Int,
    maxValue: Int,
    step: Int,
    decreaseContentDescription: String,
    increaseContentDescription: String,
    onValueChange: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        IconButton(
            onClick = { onValueChange((value - step).coerceAtLeast(minValue)) },
            enabled = value > minValue
        ) {
            Icon(Icons.Filled.Remove, contentDescription = decreaseContentDescription)
        }
        Text(
            text = minutesTemplate.format(value),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        IconButton(
            onClick = { onValueChange((value + step).coerceAtMost(maxValue)) },
            enabled = value < maxValue
        ) {
            Icon(Icons.Filled.Add, contentDescription = increaseContentDescription)
        }
    }
}
