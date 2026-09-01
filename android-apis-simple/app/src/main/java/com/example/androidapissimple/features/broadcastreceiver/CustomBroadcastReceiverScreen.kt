package com.example.androidapissimple.features.broadcastreceiver

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun CustomBroadcastReceiverScreen(
    title: String
){
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title)
        Text("Custom Broadcast receiver is handmade, is taken from the activity onStart(), being completely custom has not to be declared in the manifest manifest")
        Button(onClick = {
            val intent = Intent(CUSTOM_INTENT_ACTION)
            intent.putExtra(CUSTOM_INTENT_ACTION_KEY_EXTRA, "custom action intent sent to broadcast receiver")
            context.sendBroadcast(intent)
        }) {
            Text("Send custom action")
        }
    }
}