package com.example.simpleanimations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.simpleanimations.ui.screens.UsefulAnimationsInOneScreen
import com.example.simpleanimations.ui.theme.SimpleAnimationsTheme
import com.louis.composeplayground.ui.Custom

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleAnimationsTheme {
                // SimplestAnimationsShowcase()
                // UsefulAnimationsInOneScreen()
                //CustomTabRow()
                Custom()
            }
        }
    }
}

