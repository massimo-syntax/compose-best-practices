package com.example.notificationsadvanced.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.notificationsadvanced.MY_RECEIVER_MESSAGE_NAME
import com.example.notificationsadvanced.MainActivity
import com.example.notificationsadvanced.notification.broadcastReceiver.MyReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton



@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ActivityNotificationBuilder


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BroadcastReceiverNotificationBuilder


@Module
@InstallIn(SingletonComponent::class)
object EndNotificationProviderModule {

    @Singleton
    @Provides
    @ActivityNotificationBuilder
    fun provideNotificationBuilderToActivity(
        @ApplicationContext context: Context,
        @BaseNotificationBuilder builder: NotificationCompat.Builder
    ): NotificationCompat.Builder {

        // without intent appears just a notification, without nothing on click
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return builder
            .setContentTitle("Go to app Main Activity")
            .setContentText("This is a notification. you can click here to open the app, or go to the MainActivity")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }


    @Singleton
    @Provides
    @BroadcastReceiverNotificationBuilder
    fun provideNotificationBuilderBroadcastReceiver(
        @ApplicationContext context: Context,
        @BaseNotificationBuilder builder: NotificationCompat.Builder
    ): NotificationCompat.Builder {

        val intent = Intent(context, MyReceiver::class.java).apply {
            putExtra(MY_RECEIVER_MESSAGE_NAME, "message from broadcast receiver intent")
        }

        val broadcastFlag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O )
            PendingIntent.FLAG_IMMUTABLE
        else 0

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            broadcastFlag
        )

        return builder
            .setContentTitle("Go to app Main Activity")
            .setContentText("This is a notification. you can click here to open the app, or go to the MainActivity")
            .addAction(0, "BroadcastReceiver", pendingIntent)

    }

}