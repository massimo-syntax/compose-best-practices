package com.example.switchdarkmode

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel : ViewModel() {

    private val _themeMode = MutableStateFlow(
        // first take theme from sharedPreferences
        ThemeMode.entries.first { it.value == ThemePreferences.theme }
    )
    val themeMode = _themeMode.asStateFlow()

    fun setTheme(mode: ThemeMode){
        // save in sharedPreferences, to find at app start
        ThemePreferences.theme = mode.value
        // change theme for ui
        _themeMode.value = mode


    }

}