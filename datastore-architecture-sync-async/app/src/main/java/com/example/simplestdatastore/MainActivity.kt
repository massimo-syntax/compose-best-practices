package com.example.simplestdatastore

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.simplestdatastore.ui.theme.SimplestDatastoreTheme
import kotlinx.coroutines.launch









class MainActivity : ComponentActivity() {
    val viewModel: MainActivityViewModel by viewModels{ myViewmodelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // some application state
        val userName = mutableStateOf(
            viewModel.getPrefsSync().username
        )

        val theme = mutableStateOf(
            viewModel.getPrefsSync().theme ?: SelectedTheme.SYSTEM
        )

        try {
            lifecycleScope.launch {
                // suspension until started state is reached
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                  // some async updates
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", e.localizedMessage ?: "UNKNOWN ERROR")
        }

        enableEdgeToEdge()
        setContent {
            SimplestDatastoreTheme(shouldUseDarkTheme(theme.value) ) {
                SettingsScreen(
                    username = userName.value,
                    onUpdateTheme = { theme.value = it }
                )
            }
        }
    }
}


@Composable
private fun shouldUseDarkTheme(theme: SelectedTheme): Boolean{
    return when(theme){
        SelectedTheme.DARK -> true
        SelectedTheme.SYSTEM -> isSystemInDarkTheme()
        SelectedTheme.LIGHT -> false
    }
}

