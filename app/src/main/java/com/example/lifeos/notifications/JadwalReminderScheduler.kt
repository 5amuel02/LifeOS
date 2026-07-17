package com.example.lifeos.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.time.LocalDate
import java.time.ZoneId

/**
 * Schedules a one-time exact (or best-effort) alarm for a Jadwal item that broadcasts to
 * [JadwalReminderReceiver] at its chosen date and time. Unlike habit reminders this does not
 * repeat — a schedule item fires once, at the moment it's due.
 */
object JadwalReminderScheduler {
    const val EXTRA_ITEM_ID = "extra_jadwal_item_id"
    const val EXTRA_TITLE = "extra_jadwal_title"

    fun schedule(context: Context, itemId: Long, title: String, dateEpochDay: Long, minuteOfDay: Int) {
        val triggerAtMillis = triggerMillis(dateEpochDay, minuteOfDay)
        if (triggerAtMillis <= System.currentTimeMillis()) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, JadwalReminderReceiver::class.java).apply {
            putExtra(EXTRA_ITEM_ID, itemId)
            putExtra(EXTRA_TITLE, title)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            itemId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val canScheduleExact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }

        if (canScheduleExact) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent,
            )
        } else {
            // Falls back to an inexact alarm so we never crash with a SecurityException
            // when the exact-alarm capability has been revoked.
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent,
            )
        }
    }

    fun cancel(context: Context, itemId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, JadwalReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            itemId.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE,
        ) ?: return
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun triggerMillis(dateEpochDay: Long, minuteOfDay: Int): Long {
        return LocalDate.ofEpochDay(dateEpochDay)
            .atTime(minuteOfDay / 60, minuteOfDay % 60)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
}
