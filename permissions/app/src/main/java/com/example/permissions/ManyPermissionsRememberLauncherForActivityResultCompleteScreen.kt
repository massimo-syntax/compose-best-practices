package com.example.permissions


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun ManyPermissionsRememberLauncherForActivityResultCompleteScreen() {
    val context = LocalContext.current

    val allPermissions = mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.READ_MEDIA_IMAGES)
            add(Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val permissionStatuses = remember { mutableStateMapOf<String, Boolean>() }
    var showSettingsDialog by remember { mutableStateOf(false) }
    // disable the button
    var isPermissionRequestInProgress by remember { mutableStateOf(false) }

    // checks all permissions granted
    // updates the permissions statuses map to then display go-to-settings dialog when any is been denied
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        // Reset flag when permission request is complete
        isPermissionRequestInProgress = false
        // update permissions statuses map
        result.forEach { (permission, isGranted) ->
            permissionStatuses[permission] = isGranted
        }
        // If any permissions are permanently denied, show settings dialog
        // see in permissions statuses map permissions that are still denied
        if (allPermissions.any {
                !(permissionStatuses[it] ?: false)
                && !ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, it)
            }) {
            showSettingsDialog = true
        }
    }

    // see which permissions are already granted on launch
    // to display the initial permission statuses to display
    LaunchedEffect(Unit) {
        allPermissions.forEach { permission ->
            permissionStatuses[permission] = ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // display list of permissions
    // + button request permissions
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Permissions Status", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // display every permission name with status
        allPermissions.forEach { permission ->
            val status = permissionStatuses[permission] ?: false
            CurrentSinglePermissionStatus(permission = permission, status = status)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (!isPermissionRequestInProgress) {
                    isPermissionRequestInProgress = true
                    val deniedPermissions = allPermissions.filter { !permissionStatuses[it]!! }
                    // request permissions
                    // ALL PERMISSIONS NOT GRANTED NOW
                    // ARE REQUESTED AGAIN THROUGH THE DIALOG
                    // THATS DONE IN THE CALLBACK OF PERMISSION LAUNCER -> ACTIVITY RESULT ABOVE
                    // that answers to my previous nighmare to know when "should show rationale"
                    if (deniedPermissions.isNotEmpty()) {
                        permissionLauncher.launch(deniedPermissions.toTypedArray())
                    } else {
                        Toast.makeText(context, "All permissions granted!", Toast.LENGTH_SHORT)
                            .show()
                        isPermissionRequestInProgress = false
                    }
                }
            },
            enabled = !isPermissionRequestInProgress // Disable button if a request is in progress
        ) {
            Text("Request Permissions")
        }
    }


    if (showSettingsDialog) {
        GoToSettingsDialog(onDismiss = { showSettingsDialog = false })
    }
}


@Composable
fun GoToSettingsDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Permissions Required") },
        text = {
            Text("Some permissions are permanently denied. Please enable them from app settings.")
        },
        confirmButton = {
            Button(onClick = {
                onDismiss()
                openAppSettings(context)
            }) {
                Text("Open Settings")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

@Composable
fun CurrentSinglePermissionStatus(permission: String, status: Boolean) {
    val permissionName = when (permission) {
        Manifest.permission.CAMERA -> "Camera"
        Manifest.permission.ACCESS_FINE_LOCATION -> "Location (Fine)"
        Manifest.permission.ACCESS_COARSE_LOCATION -> "Location (Coarse)"
        Manifest.permission.READ_MEDIA_IMAGES -> "Read Media (Images)"
        Manifest.permission.READ_MEDIA_VIDEO -> "Read Media (Videos)"
        Manifest.permission.WRITE_EXTERNAL_STORAGE -> "Write External Storage"
        Manifest.permission.READ_EXTERNAL_STORAGE -> "Read External Storage"
        Manifest.permission.POST_NOTIFICATIONS -> "Notifications"
        else -> permission
    }

    val statusText = if (status) "Granted" else "Denied"
    val statusColor =
        if (status) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = permissionName)
        Text(text = statusText, color = statusColor)
    }
}