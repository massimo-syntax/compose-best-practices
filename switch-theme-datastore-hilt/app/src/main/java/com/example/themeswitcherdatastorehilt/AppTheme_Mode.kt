package com.example.themeswitcherdatastorehilt

import androidx.compose.ui.graphics.Color

data class AppTheme(
    val isDarkMode: Boolean,
    val isHighContrast: Boolean = false,
    val accentColor: Color = Color.Blue
)

sealed class ThemeMode {
    object Light : ThemeMode()
    object Dark : ThemeMode()
    object System : ThemeMode()
    object HighContrast : ThemeMode()
}