package com.example.lifeos.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.lifeos.core.strings.AppLanguage
import com.example.lifeos.core.strings.EnglishStrings
import com.example.lifeos.core.strings.IndonesianStrings
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.data.LifeOSDatabase
import com.example.lifeos.data.settings.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Receives the one-time alarm broadcast for a due Jadwal item and posts a notification,
 * unless the item was deleted or already marked done before its time arrived. Also
 * re-registers every still-pending, not-yet-due reminder after a reboot, since alarms do
 * not survive a device restart.
 */
class JadwalReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val appContext = context.applicationContext

        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED
        ) {
            rescheduleAll(appContext)
            return
        }

        val itemId = intent.getLongExtra(JadwalReminderScheduler.EXTRA_ITEM_ID, -1L)
        val title = intent.getStringExtra(JadwalReminderScheduler.EXTRA_TITLE).orEmpty()
        if (itemId < 0L) return

        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val item = LifeOSDatabase.getInstance(appContext).jadwalDao().getByIdOnce(itemId)
                if (item != null && !item.isCompleted) {
                    val strings = currentStrings(appContext)
                    withContext(Dispatchers.Main) {
                        JadwalNotifications.showReminder(appContext, strings, itemId, title)
                    }
                }
            } finally {
                pending.finish()
            }
        }
    }

    private fun rescheduleAll(context: Context) {
        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val items = LifeOSDatabase.getInstance(context).jadwalDao().getAllOnce()
                items.forEach { item ->
                    val minute = item.minuteOfDay
                    if (!item.isCompleted && minute != null) {
                        JadwalReminderScheduler.schedule(context, item.id, item.title, item.dateEpochDay, minute)
                    }
                }
            } finally {
                pending.finish()
            }
        }
    }

    private suspend fun currentStrings(context: Context): LifeOSStrings {
        val language = SettingsRepository(context).settings.first().language
        return when (language) {
            AppLanguage.INDONESIAN -> IndonesianStrings
            AppLanguage.ENGLISH -> EnglishStrings
        }
    }
}
