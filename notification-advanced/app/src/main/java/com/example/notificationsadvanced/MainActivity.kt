package com.example.notificationsadvanced

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.notificationsadvanced.presentation.screen.MainScreen
import com.example.notificationsadvanced.ui.theme.NotificationsAdvancedTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotificationsAdvancedTheme {
                MainScreen()
            }
        }
    }
}
