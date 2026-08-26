package com.example.androidapissimple.features.backgroundservice

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

@Composable
fun BackgroundServiceScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Background Service of this kind is not really needed, work manager does the same, background services are canceled when the app is closed")
        Button(onClick = {
            val intent = Intent(context, MyBackgroundService::class.java)
            context.startService(intent)
        }) {
            Text("Start")
        }
        Button(onClick = {
            val intent = Intent(context, MyBackgroundService::class.java)
            context.stopService(intent)
        }) {
            Text("Stop")
        }
    }
}
