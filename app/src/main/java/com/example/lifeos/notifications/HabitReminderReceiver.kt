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
 * Receives the per-habit alarm broadcast, posts the reminder notification and
 * schedules the next day's alarm. Also re-registers every reminder after a reboot,
 * because alarms do not survive a device restart.
 */
class HabitReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val appContext = context.applicationContext

        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED
        ) {
            rescheduleAll(appContext)
            return
        }

        val habitId = intent.getLongExtra(HabitReminderScheduler.EXTRA_HABIT_ID, -1L)
        val habitName = intent.getStringExtra(HabitReminderScheduler.EXTRA_HABIT_NAME).orEmpty()
        val minuteOfDay = intent.getIntExtra(HabitReminderScheduler.EXTRA_MINUTE_OF_DAY, -1)
        if (habitId < 0L || minuteOfDay < 0) return

        // Re-arm for the next day so the reminder repeats daily.
        HabitReminderScheduler.schedule(appContext, habitId, habitName, minuteOfDay)

        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val strings = currentStrings(appContext)
                withContext(Dispatchers.Main) {
                    HabitNotifications.showReminder(appContext, strings, habitId, habitName)
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
                val habits = LifeOSDatabase.getInstance(context).habitDao().getAllHabitsOnce()
                habits.forEach { habit ->
                    habit.reminderMinuteOfDay?.let { minute ->
                        HabitReminderScheduler.schedule(context, habit.id, habit.name, minute)
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
