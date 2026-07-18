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
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.jvm.java
import kotlin.uuid.Uuid


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseNotificationBuilder

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    private val CHANNEL_ID = Uuid.random().toString()

    @Provides
    @BaseNotificationBuilder
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Title to override")
            .setContentText("Override the content as well, if want always, could be better tho")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    @Singleton
    @Provides
    // Notification channel
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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




