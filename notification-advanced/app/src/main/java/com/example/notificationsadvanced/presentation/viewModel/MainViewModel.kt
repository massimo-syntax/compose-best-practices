package com.example.notificationsadvanced.presentation.viewModel

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notificationsadvanced.di.BaseNotificationBuilder
import com.example.notificationsadvanced.notification.notificationSource.NotificationSource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel @Inject constructor(
    @BaseNotificationBuilder
    private val notificationBuilder: NotificationCompat.Builder,
    val notificationManager: NotificationManagerCompat,
    private val notificationSource: NotificationSource
) : ViewModel() {

    val permissionGranted = notificationSource.permissionFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            false
        )


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showSimpleNotification() = notificationSource.reminderNotificationNoAction()

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun privateNotification() = notificationSource.privateReminderNotification()

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun notificationToActivity() = notificationSource.notificationToActivity()

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun broadcastReceiverNotification() =
        notificationSource.notificationBroadcastReceiver()

    fun cancelOldestNotification() = notificationSource.cancelOldestNotification()

}