package com.example.androidapissimple.features.contentProvider

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun ContentProviderScreen(
    title: String
){
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    // request permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { result ->
        if(result) permissionGranted = true
        if( // not just once denied
            !ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.READ_CONTACTS)
            && !permissionGranted
            )
        { showSettingsDialog = true }
    }

    // check permission on composable launch
    LaunchedEffect(Unit) {
        permissionGranted = ContextCompat.checkSelfPermission(
                context,
            Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
    }

    if (permissionGranted){
        ContentProviderSection(title)
    }else{
        Button({
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }){
            Text("Request permission")
        }
    }

    if (showSettingsDialog){
        GoToSettingsDialog { showSettingsDialog = false }
    }

}

@Composable
fun ContentProviderSection(
    title: String
){
    val context = LocalContext.current
    val contactsList = remember { mutableStateListOf<String>() }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadContacts(){
        val contentResolver = context.contentResolver
        // val uri  = ContactsContract.Contacts.CONTENT_URI
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE
            ),
            null,
            null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )
        // close cursor also by exception
        cursor?.use {
            // index of column contacts.
            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            if (nameIndex != -1) {
                contactsList.clear()
                // move cursor through all rows of contacts table.
                while (it.moveToNext()) {
                    val name = it.getString(nameIndex)
                    val phone = it.getString(phoneIndex)
                    contactsList.add("$name $phone")
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title)
        Text("Request contacts from content provider")
        Button(onClick = {
            loadContacts()
        }) {
            Text("Show contacts")
        }
        Text("you have ${contactsList.size} contacts")
        contactsList.forEach {
            Text(it)
        }


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

