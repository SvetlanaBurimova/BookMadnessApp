package com.example.book_madness.util.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.book_madness.BookMadnessTimerActivity
import com.example.book_madness.R
import com.example.book_madness.util.CHANNEL_ID
import com.example.book_madness.util.NOTIFICATION_CHANNEL_DESCRIPTION
import com.example.book_madness.util.NOTIFICATION_CHANNEL_NAME
import com.example.book_madness.util.NOTIFICATION_TITLE
import com.example.book_madness.util.REQUEST_CODE

fun bookReminderNotification(
    message: String,
    context: Context
) {
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(
        CHANNEL_ID,
        NOTIFICATION_CHANNEL_NAME,
        importance
    )
    channel.description = NOTIFICATION_CHANNEL_DESCRIPTION

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.createNotificationChannel(channel)

    val pendingIntent: PendingIntent = createPendingIntent(context)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.notifications)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true).build()

    notificationManager.notify(1, builder)
}

fun createPendingIntent(appContext: Context): PendingIntent {
    val intent = Intent(appContext, BookMadnessTimerActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    return PendingIntent.getActivity(
        appContext,
        REQUEST_CODE,
        intent,
        FLAG_IMMUTABLE
    )
}
