package com.example.deeplinksparsing

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deeplinksparsing.Notifications.Notifications


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Root(
    data: Uri?,
    notifications: Notifications
) {
    // empty composable that sends the notification
    var notificationPermission by remember { mutableStateOf(false) }

    RequestNotificationPermission { notificationPermission = it }

    if (notificationPermission) {
        notifications.sendNotification()
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                {
                    notifications.cancelNotifications()
                },
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
                    .background(MaterialTheme.colorScheme.background),
                ) {
                Text(
                    "cancel\n notification",
                    textAlign = TextAlign.Center,
                    fontSize = 8.sp,
                    color = Color.Blue,
                    lineHeight = 10.sp
                )
            }
        }
        MainScreen(data)
    }


}