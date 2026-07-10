package com.example.switchdarkmode

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.LocalContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        // This reads the persisted preference synchronously before any Activity launches,
        // so the first frame is already in the right mode
        AppCompatDelegate.setDefaultNightMode(
            when (ThemeMode.entries.first { it.value == ThemePreferences.theme }) {
                ThemeMode.LIGHT  -> AppCompatDelegate.MODE_NIGHT_NO
                ThemeMode.DARK   -> AppCompatDelegate.MODE_NIGHT_YES
                ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }

    companion object {
        lateinit var instance: App
            private set
    }
}