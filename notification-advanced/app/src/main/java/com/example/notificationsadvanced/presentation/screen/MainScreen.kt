package com.example.notificationsadvanced.presentation.screen

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notificationsadvanced.presentation.viewModel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {

    val permission by viewModel.permissionGranted.collectAsStateWithLifecycle()

    requestPermission(permission){
        NotificationButtons(
            viewModel::showSimpleNotification,
            viewModel::privateNotification,
            viewModel::cancelOldestNotification,
            viewModel::broadcastReceiverNotification
        )
    }



}

@Composable
fun NotificationButtons(
    onShowSimpleNotification: () -> Unit,
    onPrivateNotification: () -> Unit,
    onCancelOldestNotification: () -> Unit,
    onBroadcastReceiverNotification: ()->Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Main screen", style = MaterialTheme.typography.titleLarge)

        Button(onClick = onShowSimpleNotification) {
            Text("Show Notification")
        }

        Button(onClick = onPrivateNotification) {
            Text("Private notification")
        }

        Button(onCancelOldestNotification){
            Text("Cancel Notification")
        }
        Button(onBroadcastReceiverNotification) {
            Text("Broadcast receiver")
        }
    }
}

@Composable
fun requestPermission(
    permission: Boolean,
    content: @Composable ()->Unit
){
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            permissionGranted = true
        } else {
            Toast.makeText(context, "permission not granted", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(permission) {
        if (permission) return@LaunchedEffect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    if(permission || permissionGranted){
        content()
    }

}