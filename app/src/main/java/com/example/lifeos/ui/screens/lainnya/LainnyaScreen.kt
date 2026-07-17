package com.example.lifeos.ui.screens.lainnya

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings

private data class OtherModule(
    val title: String,
    val icon: ImageVector,
    val onClick: (() -> Unit)? = null,
)

@Composable
fun LainnyaScreen(
    onOpenSettings: () -> Unit = {},
    onOpenMoneyManager: () -> Unit = {},
    onOpenBelajar: () -> Unit = {},
    onOpenJadwal: () -> Unit = {},
    onOpenTargetHidup: () -> Unit = {},
    onOpenStatistik: () -> Unit = {},
) {
    val strings = LocalStrings.current
    val modules = buildModules(strings, onOpenSettings, onOpenMoneyManager, onOpenBelajar, onOpenJadwal, onOpenTargetHidup, onOpenStatistik)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(modules) { module ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .let { base -> if (module.onClick != null) base.clickable(onClick = module.onClick) else base }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(imageVector = module.icon, contentDescription = module.title)
                    Text(text = module.title, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

private fun buildModules(
    strings: LifeOSStrings,
    onOpenSettings: () -> Unit,
    onOpenMoneyManager: () -> Unit,
    onOpenBelajar: () -> Unit,
    onOpenJadwal: () -> Unit,
    onOpenTargetHidup: () -> Unit,
    onOpenStatistik: () -> Unit,
): List<OtherModule> = listOf(
    OtherModule(strings.lainnya.moduleSettings, Icons.Filled.Settings, onOpenSettings),
    OtherModule(strings.lainnya.moduleMoneyManager, Icons.Filled.AttachMoney, onOpenMoneyManager),
    OtherModule(strings.lainnya.moduleBelajar, Icons.Filled.School, onOpenBelajar),
    OtherModule(strings.lainnya.moduleJadwal, Icons.Filled.CalendarMonth, onOpenJadwal),
    OtherModule(strings.lainnya.moduleTargetHidup, Icons.Filled.Flag, onOpenTargetHidup),
    OtherModule(strings.lainnya.moduleMoodTracker, Icons.Filled.EmojiEmotions),
    OtherModule(strings.lainnya.moduleCloudSync, Icons.Filled.CloudSync),
    OtherModule(strings.lainnya.moduleDashboardStatistik, Icons.Filled.BarChart, onOpenStatistik),
)
