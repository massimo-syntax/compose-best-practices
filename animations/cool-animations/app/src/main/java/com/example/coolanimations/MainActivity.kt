package com.example.coolanimations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.coolanimations.animations.ContentSize
import com.example.coolanimations.animations.Visibility
import com.example.coolanimations.ui.theme.CoolAnimationsTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoolAnimationsTheme {
                //ContentSize()
                Visibility()
            }
        }
    }
}


