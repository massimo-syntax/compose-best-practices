package com.example.deeplinksbasics

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.deeplinksbasics.ui.theme.DeepLinksBasicsTheme
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri: Uri? = intent.data

        Toast.makeText(this,uri.toString(),Toast.LENGTH_SHORT).show()

        val myWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this)
            .enqueue(myWorkRequest)

        enableEdgeToEdge()
        setContent {
            DeepLinksBasicsTheme {
                Root()
            }
        }
    }


}

// test deep links
/*
    adb shell am start -W -a android.intent.action.VIEW -d "https://www.example.com/product?id=123"
*/

@Composable
fun Root(){
    NotificationsScreen()
}
