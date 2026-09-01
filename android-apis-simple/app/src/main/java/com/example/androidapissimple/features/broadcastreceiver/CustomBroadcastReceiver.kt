package com.example.androidapissimple.features.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CustomBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val data = intent?.getStringExtra(CUSTOM_INTENT_ACTION_KEY_EXTRA) ?: "intent is null"
        Toast.makeText(context, data, Toast.LENGTH_SHORT).show()
    }
}