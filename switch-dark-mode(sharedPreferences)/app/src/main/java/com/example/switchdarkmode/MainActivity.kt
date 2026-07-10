package com.example.switchdarkmode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.switchdarkmode.ui.theme.AppTheme
import com.example.switchdarkmode.ui.theme.AppThemeColor
import com.example.switchdarkmode.ui.theme.SwitchDarkModeTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        lifecycleScope.launch {
//            themeViewModel.themeMode.collect { mode ->
//                AppCompatDelegate.setDefaultNightMode(
//                    when (mode) {
//                        ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
//                        ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
//                        ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
//                    }
//                )
//            }
//        }
        setContent {
            val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()

            enableEdgeToEdge(
                statusBarStyle =
                    if( (themeMode != ThemeMode.DARK && !isSystemInDarkTheme()) || themeMode == ThemeMode.LIGHT ){
                        SystemBarStyle.light(Color.Transparent.toArgb(),Color.Transparent.toArgb())
                    }else{
                        SystemBarStyle.dark(Color.Transparent.toArgb())
                    }
            )

            AppTheme(themeMode = themeMode) {
                SettingsScreen(themeViewModel)
            }
        }
    // instead of
    //
    //  setContent {
    //      SwitchDarkModeTheme {
    //          HomeScreen()
    //      }
    //   }
    // For components that need a conditional dark/light value
    // — like an icon tint that doesn't go through AppThemeColor
    // — read LocalIsDarkTheme instead of calling back into the ViewModel:
    // val isDark = LocalIsDarkTheme.current
    // val iconTint = if (isDark) Color.White else Color.Black
    }
}

