package com.example.roomrelationsmigrations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.migrations.ui.MigrationsScreen
import com.example.roomrelationsmigrations.ui.theme.RoomRelationsMigrationsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomRelationsMigrationsTheme {
                MigrationsScreen()
            }
        }
    }
}
