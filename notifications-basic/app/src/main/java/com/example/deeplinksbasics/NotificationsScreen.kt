package com.example.deeplinksbasics

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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory


@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = viewModel(factory = NotificationsViewModel .Factory)
){

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text("that is the app screen", style = MaterialTheme.typography.titleLarge )


        Button({
            viewModel.simpleNotification("hello", "simple notification!")
        }) {
            Text("simple notification")
        }
        Button({
            viewModel.openActivity("open activity", "let see what is the url")
        }) {
            Text("open Activity")
        }
        Button({
            viewModel.deepLinkNotification("deep link!", "found !")
        }) {
            Text("deep link!")
        }
        Button({
            viewModel.scheduleReminder("scheduleReminder", "scheduleReminder contentText !")
        }) {
            Text("scheduleReminder")
        }




    }

}


