package com.example.notificationsadvanced.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationsadvanced.MY_RECEIVER_MESSAGE_NAME
import com.example.notificationsadvanced.MainActivity
import com.example.notificationsadvanced.R
import com.example.notificationsadvanced.notification.broadcastReceiver.MyReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java
import kotlin.uuid.Uuid

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    private val CHANNEL_ID = Uuid.random().toString()
    @Singleton
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        // without intent appears just a notification, without to open the app on click
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntentActivity: PendingIntent =
            PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE)

        // intent for a broadcast receiver
        val broadcastIntent = Intent(context, MyReceiver::class.java).apply {
            putExtra(MY_RECEIVER_MESSAGE_NAME, "message from broadcast receiver intent")
        }

        val broadcastFlag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O )
            PendingIntent.FLAG_IMMUTABLE
        else 0

        val pendingIntentBroadcast = PendingIntent.getBroadcast(
            context,
            0,
            broadcastIntent,
            broadcastFlag
        )


        // we just return a basic builder
        // so every function in the notification source adds to the notification whats needed

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that fires when the user taps the notification.
            // to open activity .setContentIntent(pendingIntentActivity)
            // .setAutoCancel(true)
            // broadcast message
            .addAction(0, "Action Button", pendingIntentBroadcast)
        /*  for that the user would have to unlock the private notifications in the setting
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setPublicVersion(
                NotificationCompat.Builder(context,CHANNEL_ID)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!")
                    .build()
            )
        */
    }

    @Singleton
    @Provides
    // Notification channel
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
            val channel = NotificationChannel(
                CHANNEL_ID,
                "This channel @providenotificationManager",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }

}