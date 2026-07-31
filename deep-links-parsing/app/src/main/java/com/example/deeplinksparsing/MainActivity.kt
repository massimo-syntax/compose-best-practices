package com.example.deeplinksparsing

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.deeplinksparsing.Notifications.Notifications
import com.example.deeplinksparsing.designsystem.ui.theme.DeepLinksParsingTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val notificationManager =
            Notifications(this)
                .apply { createNotificationChannel() }


        val data = intent.data
        Toast.makeText(this,"$data",Toast.LENGTH_LONG).show()

        enableEdgeToEdge()
        setContent {
            DeepLinksParsingTheme {
                Root(data, notificationManager)
            }
        }
    }
}

