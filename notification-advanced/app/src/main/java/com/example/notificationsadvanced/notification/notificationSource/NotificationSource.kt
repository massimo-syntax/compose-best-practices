package com.example.notificationsadvanced.notification.notificationSource

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationsadvanced.di.ActivityNotificationBuilder
import com.example.notificationsadvanced.di.BaseNotificationBuilder
import com.example.notificationsadvanced.di.BroadcastReceiverNotificationBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class NotificationSource @Inject constructor(
    @ActivityNotificationBuilder
    private val activityNotificationBuilder: NotificationCompat.Builder,
    @BroadcastReceiverNotificationBuilder
    private val broadcastReceiverNotificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat,
    @ApplicationContext private val context: Context,
    // also the base notificationBuildrt to customize notifications here
    // i was struggling to think if to import the context or make annotations for hilt..
    @BaseNotificationBuilder
    private val basicNotificationBuilder: NotificationCompat.Builder
) {

    private var notificationIds = mutableListOf<Int>()
    private val newNotificationId = {
        val id = Random.nextInt()
        notificationIds.add(id)
        id
    }

    val permissionFlow = flow { emit(notificationManager.areNotificationsEnabled()) }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun reminderNotificationNoAction(
        title: String = "Reminder notification",
        contentText: String = "This is just a reminder, no action button"
    ){
        val notification = basicNotificationBuilder
            .setContentTitle(title)
            .setContentText(contentText)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(
            newNotificationId(),
            notification
        )
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun privateReminderNotification(
        title: String = "Private Notification",
        contentText: String = "Lock screen"
    ){
        //for that the user would have to unlock the private notifications in the setting
        val notification = basicNotificationBuilder
            .setContentTitle(title)
            .setContentText(contentText)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setPublicVersion(
                basicNotificationBuilder
                    .setContentTitle("Notification not private")
                    .setContentText("Now you can read all!")
                    .build()
            )
            .build()
        notificationManager.notify(
            newNotificationId(),
            notification
        )
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun notificationToActivity(
        title: String = "Notification to activity",
        contentText :String = "Click here to go to the Main Activity, the app is there..."
    ){
        val notification = activityNotificationBuilder
            .setContentTitle(title)
            .setContentText(contentText)
            .build()
        notificationManager.notify(
            newNotificationId(),
            notification
        )
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun notificationBroadcastReceiver(
        title: String = "Broadcast Receiver Notification",
        contentText :String = "That print a toast form the broadcast receiver, no data from the broadcast receiver here, or in the screen"
    ) {
        val notification = broadcastReceiverNotificationBuilder
            .setContentTitle(title)
            .setContentText(contentText)
            .build()
        notificationManager.notify(
            newNotificationId(),
            notification
        )
    }


    fun cancelOldestNotification(){
        if(notificationIds.isEmpty()) return
        notificationManager.cancel( notificationIds.first() )
        notificationIds.removeFirstOrNull()
    }


}