package com.example.deeplinksparsing.Notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.deeplinksparsing.R

class Notifications (private val context: Context){
    private val CHANNEL_ID = "92034j57209457rd02k9"

    fun createNotificationChannel() {
        // Only necessary for API 26+ (Android 8.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My App Notifications"
            val descriptionText = "This channel is used for general alerts"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            // THE ID MUST BE THE SAME AS YOU USE IN THE BUILDER ("CHANNEL_ID_EJEMPLO")
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification() : Notification {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "deeplinkapp://myapp/user/123".toUri(),
        )
        val pendingIntent = PendingIntent.getActivity(context, 8234689, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification =  NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("deep link")
            .setContentText("click so you can oen the app with deep link")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // required
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_STOPWATCH)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
        return notification.build()
    }

    fun sendNotification(){
        val notification = createNotification()
        NotificationManagerCompat.from(context).notify(Int.MAX_VALUE, notification)
    }


    fun cancelNotifications(){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }


}

// pending intent
/*
    FLAG_UPDATE_CURRENT: This flag indicates that if the described pending intent already exists, its extra data should be updated with the new intent's extra data.
    FLAG_CANCEL_CURRENT: If a pending intent with the same description already exists, it will be canceled before the new one is created.
    FLAG_ONE_SHOT: This flag specifies that the pending intent can only be used once. After it is launched, it will be automatically canceled.
    FLAG_NO_CREATE: If a pending intent with the same description does not already exist, it will not be created, and the method will return null.
 */