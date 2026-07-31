package com.example.deeplinksparsing


import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RequestNotificationPermission(onPermission: (Boolean) -> Unit) {

    var showDialog by remember { mutableStateOf(false) }

    val requestPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionGranted ->
        if (permissionGranted) {
            onPermission(true)
        } else {
            onPermission(false)
            showDialog = true
        }
    }

    LaunchedEffect(Unit) {
        requestPermission.launch((Manifest.permission.POST_NOTIFICATIONS))
    }

    if(showDialog){
        AlertDialogComponent{ showDialog = false }
    }

}

@Composable
fun AlertDialogComponent(
    onShowDialog: (Boolean) -> Unit
) {

    // To check if the dialog is in show
    // Alert Dialog Box
    AlertDialog(
        // set dismiss request
        onDismissRequest = { onShowDialog(false) },
        // configure confirm button
        confirmButton = {
            Button(onClick = { onShowDialog(false) }) {
                // set button text
                Text("Confirm")
            }
        },
        // configure dismiss button
        dismissButton = {
            TextButton(onClick = {
                onShowDialog(false)
            }) {
                // set button text
                Text("dismiss")
            }
        },
        // set icon
        icon = {
            Text("X", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.Red)
        },
        // set title text
        title = {
            Text(text = "Permission needed", color = Color.Black)
        },
        // set description text
        text = {
            Text(text = "you can also go to settings...", color = Color.DarkGray)
        },
        // set padding for contents inside the box
        modifier = Modifier.padding(16.dp),
        // define box shape
        shape = RoundedCornerShape(16.dp),
        // set box background color
        containerColor = Color.White,
        // set icon color
        iconContentColor = Color.Red,
        // set title text color
        titleContentColor = Color.Black,
        // set text color
        textContentColor = Color.DarkGray,
        // set elevation
        tonalElevation = 8.dp,
        // set properties
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    )


}




