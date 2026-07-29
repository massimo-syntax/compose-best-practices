package com.example.permissions.contextcompatCheckselfpermission

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.permissions.permissionstateLaunchpermissionrequest.Column

@Composable
fun ContextCompatCheckSelfPermission() {
    val context = LocalContext.current
    val activity = context as ComponentActivity


    var isGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val showRationale by remember {
        mutableStateOf(
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CALL_PHONE
            )
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            isGranted = granted
            if (granted) {
                Toast.makeText(context, "permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Column(
        Modifier.statusBarsPadding()
    ) {
        // user denied once, does not even work..
        if (showRationale) {
            Text("please allow permission, is required..")
        }

        Button({
            if (!isGranted) {
                launcher.launch(Manifest.permission.CALL_PHONE)
            }else{
                Toast.makeText(context, "permission is granted", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Request Permission")
        }

    }


}