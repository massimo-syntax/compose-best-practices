package com.example.deeplinksbasics

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random

class Notification(private val context: Context) {
    private val CHANNEL_ID = "lsdkjflasdjflasdjföl"

    // that crashes.
//    init{
//        createNotificationChannel()
//        this.builder = getNotificationBuilder()
//    }
    // Bug:
    // The docs assume that this line of code is in a method of some implementation of Context, such as an Activity or Service.
    //    Move this code into a method of some implementation of Context, such as an Activity or Service, or
    //    Call getSystemService() on some instance of Context

    // so next would I paste everyting in a : Application() class or used with hilt
    // for now is already nice to do that:

    /*
    * Google does like this.. has a notification builder for every notification
    fun showNotification(context: Context) {
       val builder = NotificationCompat.Builder(context, CHANNEL_ID)
       val notificationId = 1
       with(NotificationManagerCompat.from(context)) {
           if (ActivityCompat.checkSelfPermission(
                   context,
                   Manifest.permission.POST_NOTIFICATIONS
               ) != PackageManager.PERMISSION_GRANTED
           ) {
               // TODO: Consider calling ActivityCompat#requestPermissions here
           }
           notify(notificationId, builder.build())
       }
    }
    * https://github.com/android/snippets/blob/2bffe3fadca4f8147a62346f4eeb2f4675d83c88/compose/snippets/src/main/java/com/example/compose/snippets/notifications/NotificationsSnippets.kt#L148-L171
    */


    private fun getNotificationBuilder(context: Context): NotificationCompat.Builder {
        val textTitle = "Title"
        val textContent = "Content"

        // Create an explicit intent for an Activity in your app.
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that fires when the user taps the notification.
            .setContentIntent(pendingIntent)
            // This code calls setAutoCancel(), which automatically removes the notification when the user taps it.
            .setAutoCancel(true)
    }


    fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel name"
            val descriptionText = " that is the description of the channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system.
            val notificationManager: NotificationManager =
                context.getSystemService(NotificationManager::class.java) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }


    fun makeNotification() {
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling ActivityCompat#requestPermissions here
                // to request the missing permissions, and then overriding
                // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                //                                        grantResults: IntArray)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return@with
            }
            // notificationId is a unique int for each notification that you must define.
            val notificationId = Random.nextLong().toInt()
            val builder = getNotificationBuilder(context)
            notify(notificationId, builder.build())
        }
    }


}