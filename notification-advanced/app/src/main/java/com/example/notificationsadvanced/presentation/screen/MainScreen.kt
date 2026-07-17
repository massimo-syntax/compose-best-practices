package com.example.notificationsadvanced.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.notificationsadvanced.presentation.viewModel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
){
    Column(
        Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Main screen", style = MaterialTheme.typography.titleLarge)
        Button(onClick = viewModel::showSimpleNotification ) {
            Text("Show Notification")
        }
        // from now on the notifications have this titile
        // the builder is modified
        Button(onClick = { viewModel.updateNotification("New Title") }) {
            Text("New Notification style from now on")
        }
        Button(viewModel::cancelOldestNotification){
            Text("Cancel Notification")
        }
        Button(viewModel::newNotificationsAlsoInLockScreen){
            Text("newNotificationsAlsoInLockScreen")
        }

    }
}