package com.example.permissions.permissionstateLaunchpermissionrequest

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MultiplePermissionsLaunchPermissionRequest(){

    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
        )
    )

    if(multiplePermissionsState.allPermissionsGranted){
        Text("All Permissions Granted")
    } else {
        LaunchedEffect(Unit) {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }

        Column(Modifier.statusBarsPadding()){
            if(multiplePermissionsState.shouldShowRationale){
                Text("You need to grant all permissions\n" +
                "no way to know if the permission is ever been asked")
                Text("If the popup does not show please go to settings")
            }
        }

    }

}