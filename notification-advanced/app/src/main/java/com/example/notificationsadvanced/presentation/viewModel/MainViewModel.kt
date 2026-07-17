package com.example.notificationsadvanced.presentation.viewModel

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.example.notificationsadvanced.R
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat
) : ViewModel() {

    private var notificationIds = mutableListOf<Int>()
    private val newNotificationId = {
        val id = Random.nextInt()
        notificationIds.add(id)
        id
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showSimpleNotification(){
        notificationManager.notify(newNotificationId(),notificationBuilder.build())
    }

    // from now on the notifications have this title
    // the builder is modified
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun updateNotification(title:String = "Title From MainViewModel"){
        notificationManager.notify(
            newNotificationId(),
            notificationBuilder
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()
        )
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun newNotificationsAlsoInLockScreen(){
        notificationManager.notify(
            newNotificationId(),
            notificationBuilder
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()
        )
    }

    fun cancelOldestNotification(){
        if(notificationIds.isEmpty()) return
        notificationManager.cancel( notificationIds.first() )
        notificationIds.removeFirstOrNull()
    }

}