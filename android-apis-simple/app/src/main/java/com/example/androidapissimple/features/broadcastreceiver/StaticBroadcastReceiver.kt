package com.example.androidapissimple.features.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class StaticBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // extract data by key from Intent
        val message = intent?.getStringExtra(CUSTOM_STATIC_KEY) ?: "intent = null"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}