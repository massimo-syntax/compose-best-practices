package com.example.androidapissimple.features.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SystemEventBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // !! key MUST BE  "state"
        val airplaneModeOn = intent?.getBooleanExtra("state",false)
        Toast.makeText(context, "airplane mode is on: $airplaneModeOn", Toast.LENGTH_SHORT).show()
    }
}