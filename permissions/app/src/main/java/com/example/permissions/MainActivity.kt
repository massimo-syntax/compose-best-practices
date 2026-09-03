package com.example.permissions

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.permissions.activityresultcontract.MultiplePermissionsActivityResultContract
import com.example.permissions.activityresultcontract.SinglePermissionActivityResultContract
import com.example.permissions.contextcompatCheckselfpermission.ContextCompatCheckSelfPermission
import com.example.permissions.permissionstateLaunchpermissionrequest.CameraLaunchPermissionRequest
import com.example.permissions.permissionstateLaunchpermissionrequest.MultipleLocationPermissionsLaunchPermissionRequest
import com.example.permissions.permissionstateLaunchpermissionrequest.MultiplePermissionsLaunchPermissionRequest
import com.example.permissions.permissionstateLaunchpermissionrequest.NotificationLaunchPermissionRequest_FromApiVersionTiramisu
import com.example.permissions.ui.theme.PermissionsTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    // for Notifications

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PermissionsTheme {
                // pretty...
                // CameraLaunchPermissionRequest()
                // NotificationLaunchPermissionRequest_FromApiVersionTiramisu()
                // MultipleLocationPermissionsLaunchPermissionRequest()
                // MultiplePermissionsLaunchPermissionRequest()
                // MultiplePermissionsLaunchPermissionRequest()

                // there is better
                // ContextCompatCheckSelfPermission()

                // this is yet better
                // SinglePermissionActivityResultContract()
                // MultiplePermissionsActivityResultContract()

                // activity result contract
                // full implementation of use cases
                ManyPermissionsRememberLauncherForActivityResultCompleteScreen()
            }
        }
    }
}

// note:
// Google officially discourages apps from obtaining full storage access, recommending their file picker instead
