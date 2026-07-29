package com.example.permissions.permissionstateLaunchpermissionrequest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


// requires: com.google.accompanist:accompanist-permissions:<version>

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MultipleLocationPermissionsLaunchPermissionRequest(){

    // ONLY fine location is required to be granted from user.
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // ONLY fine location is required to be granted from user.
    if (locationPermissionsState.allPermissionsGranted) {
        Text("Thanks! I can access your exact location :D")
    } else {
        Column {
            val revoked = locationPermissionsState.revokedPermissions

            // ONLY fine location is required to be granted from user.
            // !! in this case shouldShowRationale is true only when the permission is revoked once !
            val textToShow = if (locationPermissionsState.shouldShowRationale) {
                // permission denied once
                "Permission denied once, please grant the permission of fine location,"
            } else {
                // app freshly installed, or user denied more tha twice
                "app freshly installed, or user denied more tha twice"
            }

            val buttonText = if (revoked.isNotEmpty()) {
                "Allow precise location"
            } else {
                "Request permissions"
            }

            Text(text = textToShow)
            Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
                Text("shouldShowRationale " + locationPermissionsState.shouldShowRationale.toString())
            }
        }
    }





}

@Composable
fun Column(content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}