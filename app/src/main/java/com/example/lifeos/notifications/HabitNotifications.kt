package com.example.lifeos.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.lifeos.MainActivity
import com.example.lifeos.R
import com.example.lifeos.core.strings.LifeOSStrings

/** Builds and posts the daily habit reminder notifications. */
object HabitNotifications {
    const val CHANNEL_ID = "habit_reminders"

    fun ensureChannel(context: Context, strings: LifeOSStrings) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            strings.habitReminder.habitReminderChannelName,
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = strings.habitReminder.habitReminderChannelDescription
        }
        manager.createNotificationChannel(channel)
    }

    fun showReminder(context: Context, strings: LifeOSStrings, habitId: Long, habitName: String) {
        ensureChannel(context, strings)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            // Without the runtime permission the notification would be silently dropped.
            return
        }

        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentIntent = PendingIntent.getActivity(
            context,
            habitId.toInt(),
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_habit_reminder)
            .setContentTitle(strings.habitReminder.habitReminderNotificationTitle)
            .setContentText(strings.habitReminder.habitReminderNotificationTextTemplate.format(habitName))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .build()

        NotificationManagerCompat.from(context).notify(habitId.toInt(), notification)
    }
}
