package com.example.fcm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fcm.data.TokenDataStore
import com.example.fcm.screens.FCMHomeScreen
import com.example.fcm.screens.MainViewModel
import com.example.fcm.ui.theme.FCMTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tokenDataStore = TokenDataStore(applicationContext)

        setContent {
            FCMTheme {
                val mainViewModel: MainViewModel = viewModel(
                    factory = MainViewModel.provideFactory(tokenDataStore)
                )

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FCMHomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        mainViewModel = mainViewModel
                    )
                }
            }
        }
    }
}
