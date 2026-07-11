package com.example.themeswitcherdatastorehilt.ui.theme

import androidx.compose.ui.graphics.Color
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.themeswitcherdatastorehilt.ThemeMode
import com.example.themeswitcherdatastorehilt.ThemeViewModel

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

fun highContrastColorScheme(isDark: Boolean): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = Color.White,
            onPrimary = Color.Black,
            primaryContainer = Color.White,
            onPrimaryContainer = Color.Black,
            surface = Color.Black,
            onSurface = Color.White,
            background = Color.Black,
            onBackground = Color.White,
            outline = Color.White,
            outlineVariant = Color.White
        )
    } else {
        lightColorScheme(
            primary = Color.Black,
            onPrimary = Color.White,
            primaryContainer = Color.Black,
            onPrimaryContainer = Color.White,
            surface = Color.White,
            onSurface = Color.Black,
            background = Color.White,
            onBackground = Color.Black,
            outline = Color.Black,
            outlineVariant = Color.Black
        )
    }
}

@Composable
fun ThemeSwitcherDataStoreHiltTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


@Composable
fun AppTheme(
    themeManager: ThemeViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val currentTheme by themeManager.currentTheme.collectAsState()
    val themeMode by themeManager.themeMode.collectAsState()

    val dark  = isSystemInDarkTheme()

    LaunchedEffect(dark) {
        themeManager.systemThemeSwitched(dark)
    }

    val colors = if (currentTheme.isHighContrast) {
        highContrastColorScheme(currentTheme.isDarkMode)
    } else if (currentTheme.isDarkMode) {
        darkColorScheme(primary = currentTheme.accentColor)
    } else {
        lightColorScheme(primary = currentTheme.accentColor)
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}