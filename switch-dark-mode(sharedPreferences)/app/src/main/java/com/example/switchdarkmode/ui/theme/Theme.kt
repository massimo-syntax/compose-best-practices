package com.example.switchdarkmode.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.switchdarkmode.ThemeMode

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

@Composable
fun SwitchDarkModeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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

// custom theme switcher

object AppThemeColor {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCustomColors.current
}
// Screens use AppThemeColor.colors.cardBackgroundColor
// instead of MaterialTheme.colorScheme.surface.
// MaterialTheme is still available

@Composable
fun AppTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT  -> false
        ThemeMode.DARK   -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val customColors = if (darkTheme) DarkAppColors else LightAppColors

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary    = Color(0xFF2C3638),
            secondary  = Color(0xFFBDBDBD),
            background = Color(0xFF1E1F25),
            surface    = Color(0xFF1E1F25),
            onPrimary  = Color.White,
            error      = Color(0xFFB00020),
        )
    } else {
        lightColorScheme(
            primary    = Color(0xFFFBFBFB),
            secondary  = Color(0xFF276EF7),
            background = Color(0xFFFBFBFB),
            surface    = Color(0xFFFBFBFB),
            onPrimary  = Color.Black,
            error      = Color(0xFFB00020),
        )
    }

    MaterialTheme(colorScheme = colorScheme) {
        CompositionLocalProvider(
            // use custom colors, not the ones defined for MaterialColorScheme
            LocalCustomColors provides customColors,
            // fist have darkTheme = false
            // main activity is going to check first from the viewmodel
            LocalIsDarkTheme provides darkTheme,
        ) {
            content()
        }
    }
}
