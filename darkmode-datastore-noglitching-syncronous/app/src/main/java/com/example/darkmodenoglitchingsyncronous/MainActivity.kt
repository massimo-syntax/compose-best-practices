package com.example.darkmodenoglitchingsyncronous

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.darkmodenoglitchingsyncronous.ui.theme.DarkModeNoGlitchingSyncronousTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

// DataStore key
val USER_THEME = booleanPreferencesKey("user_theme")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Reading theme
        val themeFlow: Flow<Boolean> = dataStore.data
            .map { preferences ->
                preferences[USER_THEME] ?: false
            }
        // run blocking result of flow
        val themeState: MutableState<Boolean> = runBlocking {  mutableStateOf(themeFlow.first())}

        enableEdgeToEdge()
        setContent {
            DarkModeNoGlitchingSyncronousTheme(themeState.value) {
                Screen(
                    updateTheme = {themeState.value = it}
                )
            }
        }
    }
}

@Composable
fun Screen(
    updateTheme: (Boolean)->Unit
) {
    // Scope to toggle theme on click
    val scope = rememberCoroutineScope()
    val datastore = LocalContext.current.dataStore

    // Toggle and save theme
    suspend fun saveTheme(dark: Boolean) {
        datastore.edit { settings ->
            val currentTheme = settings[USER_THEME] ?: false
            settings[USER_THEME] = dark
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "text",
            fontSize = 48.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Button(
            onClick = {
                scope.launch {
                    saveTheme(dark = false)
                    updateTheme(false)
                }
            },
        ) {
            Text("light mode")
        }

        Button(
            onClick = {
                scope.launch {
                    saveTheme(dark = true)
                    updateTheme(true)
                }
            },
        ) {
            Text("dark mode")
        }
    }
}