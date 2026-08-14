package com.example.testingtddsimple

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.testingtddsimple.screen.FirstScreen
import com.example.testingtddsimple.ui.theme.TestingtddsimpleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestingtddsimpleTheme {
                FirstScreen()
            }
        }
    }
}

