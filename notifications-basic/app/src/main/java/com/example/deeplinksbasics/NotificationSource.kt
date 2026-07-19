package com.example.deeplinksbasics

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import kotlin.random.Random

class NotificationSource(private val context: Context) {
    fun simpleNotification(title: String, contentText: String) {
        val builder = notificationBuilder(title, contentText)
            .build()
        notify(builder)
    }

    fun activityIntentNotification(title: String, contentText: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = notificationBuilder(title, contentText)
            .setContentIntent(pendingIntent)
            .build()
        notify(builder)
    }

    fun deepLinkNotification(title: String, contentText: String) {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "https://www.example.com/profile".toUri(),
            context,
            MainActivity::class.java
        )
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val builder = notificationBuilder(title, contentText)
            .setContentIntent(pendingIntent)
            .build()
        notify(builder)
    }


    // private functions
    private val CHANNEL_ID = "983475J09823742J0983R29"
    private fun notificationBuilder(
        title: String,
        contentText: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
    }

    @SuppressLint("MissingPermission") // or the ide signs error, even when none
    private fun notify(builder: Notification) {
        with(NotificationManagerCompat.from(context)) {
            val id = Random.nextInt()
            notify(id, builder)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel name: $CHANNEL_ID"
            val descriptionText = " that is the description of the channel $CHANNEL_ID"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                context.getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    init {
        createNotificationChannel()
    }

}