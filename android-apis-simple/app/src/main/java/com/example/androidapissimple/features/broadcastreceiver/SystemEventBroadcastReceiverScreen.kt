package com.example.androidapissimple.features.broadcastreceiver

import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun SystemEventBroadcastReceiverScreen(
    title: String
){
    val context = LocalContext.current
    // that is in the manifest
    val receiver = SystemEventBroadcastReceiver()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title)
        Text("Register broadcast receiver to listen system events, dynamically created by: val receiver = SystemEventBroadcastReceiver()")
        Text("TOGGLE AIRPLANE MODE !")
        Text("Broadcast receiver has to be declared in the manifest")
        DisposableEffect(context) {
            val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            context.registerReceiver(receiver, intentFilter)
            onDispose {
                context.unregisterReceiver(receiver)
            }
        }
    }
}