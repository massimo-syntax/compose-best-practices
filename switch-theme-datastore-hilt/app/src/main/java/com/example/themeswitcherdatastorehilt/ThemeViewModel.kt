package com.example.themeswitcherdatastorehilt

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _currentTheme = MutableStateFlow(AppTheme(isDarkMode = false))
    val currentTheme: StateFlow<AppTheme> = _currentTheme.asStateFlow()

    private val _themeMode = MutableStateFlow<ThemeMode>(ThemeMode.System)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    var systemDark = false

    fun systemThemeSwitched(dark: Boolean) {
        systemDark = dark
        if(_themeMode.value is ThemeMode.System){
            updateActualTheme(ThemeMode.System)
        }
    }

    fun updateThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        // Save to DataStore for persistence
        saveThemeMode(mode)
        // Update actual theme based on system state
        updateActualTheme(mode)
    }

    private fun shouldUseDark(
        mode: ThemeMode,
    ): Boolean =
        when (mode) {
            is ThemeMode.Light -> false
            is ThemeMode.Dark -> true
            is ThemeMode.System -> systemDark ?: false
            is ThemeMode.HighContrast -> _currentTheme.value.isDarkMode
        }

    private fun updateActualTheme(mode: ThemeMode) {
        // val isSystemDark = isSystemInDarkTheme()
        val shouldUseDark = shouldUseDark(mode)
        _currentTheme.value = _currentTheme.value.copy(
            isDarkMode = shouldUseDark,
            isHighContrast = mode is ThemeMode.HighContrast
        )
    }


    // no other repositories here or data sources
    // kept very very simple

    val SYSTEM = "q084urqc08ur"
    val DARK = "9qw8urqw0eurq0wr"
    val LIGHT = "w04duwß9e8u4t"
    val HIGH_CONTRAST = "98435u94rmßd"
    val themeKey = stringPreferencesKey(name = "theme")

    fun saveThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            when (mode) {
                is ThemeMode.Dark -> dataStore.edit { it[themeKey] = DARK }
                is ThemeMode.Light -> dataStore.edit { it[themeKey] = LIGHT }
                is ThemeMode.System -> dataStore.edit { it[themeKey] = SYSTEM }
                is ThemeMode.HighContrast -> dataStore.edit { it[themeKey] = HIGH_CONTRAST }
            }
        }
    }

    val EMPTY = ""
    fun getDataStore(): Flow<String> = dataStore.data.map { it[themeKey] ?: EMPTY }

    init {
        viewModelScope.launch {
            getDataStore().collect {
                when (it) {
                    SYSTEM -> {
                        _themeMode.value = ThemeMode.System
                        updateActualTheme(ThemeMode.System)
                    }
                    DARK -> {
                        _themeMode.value = ThemeMode.Dark
                        updateActualTheme(ThemeMode.Dark)
                    }
                    LIGHT -> {
                        _themeMode.value = ThemeMode.Light
                        updateActualTheme(ThemeMode.Light)
                    }
                    HIGH_CONTRAST -> {
                        _themeMode.value = ThemeMode.HighContrast
                        updateActualTheme(ThemeMode.HighContrast)
                    }
                    EMPTY -> {
                        _themeMode.value = ThemeMode.System
                        updateActualTheme(ThemeMode.System)
                    }
                }
            }
        }
    }


}
