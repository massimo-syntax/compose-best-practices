package com.example.deeplinksbasics

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters


class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    private val notifications = NotificationSource(context)

    override suspend fun doWork(): Result {
        return try {
            notifications.simpleNotification(
                inputData.getString("TITLE") ?: "not title, null",
                inputData.getString("CONTENT_TEXT") ?: "no contentText, null"
            )
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
