package com.example.androidapissimple.features.broadcastreceiver

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.androidapissimple.features.backgroundservice.MyBackgroundService


@Composable
fun StaticBroadcastReceiverScreen(
    title:String
){
    val context = LocalContext.current
    // that is in the manifest
    val staticActionId = "anyid.com.example.androidapissimple.features.broadcastreceiver.STATIC_ACTION"
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title)
        Text("Broadcast receiver is used to receive data from intent of other applications, or in this case the same application")
        Text("Broadcast receiver has to be declared in the manifest")
        Button(onClick = {
            val intent = Intent(staticActionId).apply {
                putExtra(CUSTOM_STATIC_KEY, "static event sent, received successfully from static broadcast receiver")
                // implicit broadcast have to target the own/same app
                setPackage(context.packageName)
            }
            context.sendBroadcast(intent)
        }) {
            Text("Send Broadcast")
        }
    }
}
