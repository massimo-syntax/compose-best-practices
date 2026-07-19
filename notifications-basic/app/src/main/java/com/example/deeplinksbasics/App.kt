package com.example.deeplinksbasics

import android.app.Application
import android.content.Context


class MyApplication : Application() {

    lateinit var notifications: NotificationSource

    override fun onCreate() {
        super.onCreate()
        notifications = NotificationSource(applicationContext)
    }
}
