package com.example.permissions.permissionstateLaunchpermissionrequest


import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


// requires: com.google.accompanist:accompanist-permissions:<version>

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationLaunchPermissionRequest_FromApiVersionTiramisu(){
    val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

    if (notificationPermissionState.status.isGranted) {
        Text("Camera permission Granted")
    } else {
        Column(Modifier.statusBarsPadding()) {
            val textToShow = if (notificationPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "Please grant the permission, you are still in time before to go manually to settings"
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "please grant the permission"+
                        "if nothing happens (normally here..) clicking the button, go to settings "
            }

            // !! ATTENTION, NO WAY TO KNOW IF THE USER DENIED THE PERMISSION OR IS THE FIRST TIME IN THE APP
            Text(textToShow)
            Button(onClick = { notificationPermissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }
    }
}