package com.example.deeplinksbasics

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.example.deeplinksbasics.ui.theme.DeepLinksBasicsTheme


fun log(message: Any?){
    Log.e("LOG", message.toString())
}

class MainActivity : ComponentActivity() {
    private val notify = Notification(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notify.createNotificationChannel()
        // deep link
        val intentAction: String? = intent?.action
        val intentData: Uri? = intent?.data
        enableEdgeToEdge()
        setContent {
            DeepLinksBasicsTheme {
                Display(
                    intentAction = intentAction,
                    data = intentData,
                )
                Button({
                    notify.makeNotification()
                }) { Text("notification") }
            }
        }
    }
}
