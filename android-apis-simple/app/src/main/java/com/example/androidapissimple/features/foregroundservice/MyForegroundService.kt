package com.example.androidapissimple.features.foregroundservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds

class MyForegroundService : Service() {

    enum class Action { START, STOP }

    private val tag = "MyForegroundService"
    private val channelId = "foreground_service_channel"
    private val notificationId = 1

    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private var job: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "::onStartCommand(), Service Started", Toast.LENGTH_SHORT).show()
        when (intent?.action) {
            Action.START.name -> startForegroundWork()
            Action.STOP.name -> stopForegroundWork()
            null -> Log.d(tag, "No action provided")
        }

        return START_NOT_STICKY
    }

    private fun startForegroundWork() {
        Log.d(tag, "Starting foreground work")
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Foreground Service")
            .setContentText("Service is running and logging every 2s")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(notificationId, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(notificationId, notification)
        }

        if (job == null) {
            job = scope.launch {
                while (true) {
                    Log.d(tag, "The foreground service is still on")
                    delay(2.seconds)
                }
            }
        }
    }

    private fun stopForegroundWork() {
        Log.d(tag, "Stopping foreground work")
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        Toast.makeText(this, "::onDestroy(), Service Destroyed", Toast.LENGTH_SHORT).show()
        Log.d(tag, "Foreground Service Destroyed")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
