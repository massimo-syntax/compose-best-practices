package com.example.notificationsadvanced.notification.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.notificationsadvanced.MY_RECEIVER_MESSAGE_NAME


// receiver must also be noted in the manifest file
class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra(MY_RECEIVER_MESSAGE_NAME)
        if(message != null){
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}