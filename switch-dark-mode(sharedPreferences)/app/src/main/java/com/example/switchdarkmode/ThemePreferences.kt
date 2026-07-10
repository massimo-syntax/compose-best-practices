package com.example.switchdarkmode

import android.content.Context
import androidx.core.content.edit


object ThemePreferences {
    private const val PREFS_NAME = "com.example.switchdarkmode.theme_prefs"
    private const val KEY = "theme_mode"
    // private const val DEFAULT = ThemeMode.SYSTEM.value
    // error: Const 'val' initializer must be a constant value.
    private val DEFAULT = ThemeMode.SYSTEM.value

    private fun prefs() = App.instance.baseContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var theme: Int
        get() = prefs().getInt(KEY, DEFAULT)
        set(value) { prefs().edit { putInt(KEY, value) } }
}