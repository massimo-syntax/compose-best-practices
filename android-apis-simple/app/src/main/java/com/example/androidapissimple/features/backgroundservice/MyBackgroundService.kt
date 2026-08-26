package com.example.androidapissimple.features.backgroundservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MyBackgroundService : Service() {
    private var loggingJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private val tag = "MyBackgroundService"
    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "Service Created")
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(tag, "Service Started")
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()
        // log every 2 seconds
        if(loggingJob == null){ // init job
            loggingJob = scope.launch {
                while (true) {
                    Log.d(tag, "the background service is still on")
                    delay(2.seconds)
                }
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        Log.d(tag, "Service canceled")
        Toast.makeText(this, "Service canceled", Toast.LENGTH_SHORT).show()
    }
}
