package com.example.deeplinksbasics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit


class NotificationsViewModel(
    private val notificationSource: NotificationSource,
    private val workManager: WorkManager
) : ViewModel() {

    fun simpleNotification(title:String, contentText:String) =
        notificationSource.simpleNotification(title,contentText)

    fun openActivity(title:String, contentText:String) =
        notificationSource.activityIntentNotification(title,contentText)

    fun deepLinkNotification(title:String, contentText:String) =
        notificationSource.deepLinkNotification(title,contentText)

    fun scheduleReminder(title:String, contentText:String) {
        val myWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "TITLE" to title,
                    "CONTENT_TEXT" to contentText
                )
            )
            .build()
        workManager.enqueue(myWorkRequest)
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MyApplication
                val notifications = app.notifications
                val workManager = WorkManager.getInstance(app.applicationContext)
                NotificationsViewModel(notifications, workManager)
            }
        }
    }

}