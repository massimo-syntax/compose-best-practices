package com.example.androidapissimple.features.foregroundservice

import android.Manifest
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ForegroundServiceScreen() {
    val context = LocalContext.current

    // Request notification permission for Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if(granted) Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            else Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Foreground Service sends a notification to the user when it is running.")
        Text("Requires some permissions in the manifest")

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val intent = Intent(context, MyForegroundService::class.java)
            intent.action = MyForegroundService.Action.START.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }) {
            Text("Start Foreground")
        }
        Button(onClick = {
            val intent = Intent(context, MyForegroundService::class.java).apply {
                action = MyForegroundService.Action.STOP.name
            }
            context.startService(intent)
        }) {
            Text("Stop Foreground")
        }
    }
}
