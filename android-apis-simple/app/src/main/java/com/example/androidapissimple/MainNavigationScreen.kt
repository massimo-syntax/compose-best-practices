package com.example.androidapissimple

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidapissimple.features.backgroundservice.BackgroundServiceScreen
import com.example.androidapissimple.features.broadcastreceiver.CustomBroadcastReceiverScreen
import com.example.androidapissimple.features.broadcastreceiver.StaticBroadcastReceiverScreen
import com.example.androidapissimple.features.broadcastreceiver.SystemEventBroadcastReceiver
import com.example.androidapissimple.features.broadcastreceiver.SystemEventBroadcastReceiverScreen
import com.example.androidapissimple.features.foregroundservice.ForegroundServiceScreen

enum class AppFeature(val title: String) {
    BackgroundService("Background Service"),
    ForegroundService("Foreground Service"),
    StaticBroadcastReceiver("Static Broadcast Receiver"),
    SystemEventBroadcastReceiver("System Event Broadcast Receiver"),
    CustomBroadcastReceiver("Custom Broadcast Receiver"),
    Feature4("Feature 4"),
    Feature5("Feature 5")
}

@Composable
fun MainNavigationScreen() {
    var selectedFeature by remember { mutableStateOf(AppFeature.BackgroundService) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Content Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (selectedFeature) {
                AppFeature.BackgroundService -> BackgroundServiceScreen()
                AppFeature.ForegroundService -> ForegroundServiceScreen()
                AppFeature.StaticBroadcastReceiver -> StaticBroadcastReceiverScreen(selectedFeature.title)
                AppFeature.SystemEventBroadcastReceiver -> SystemEventBroadcastReceiverScreen(selectedFeature.title)
                AppFeature.CustomBroadcastReceiver -> CustomBroadcastReceiverScreen(selectedFeature.title)
                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text(text = "Content for ${selectedFeature.title}")
                    }
                }
            }
        }

        // Custom Scrollable "Navbar"
        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            modifier = Modifier.navigationBarsPadding()
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(AppFeature.entries.toTypedArray()) { feature ->
                    FilterChip(
                        selected = selectedFeature == feature,
                        onClick = { selectedFeature = feature },
                        label = { Text(feature.title) }
                    )
                }
            }
        }
    }
}
