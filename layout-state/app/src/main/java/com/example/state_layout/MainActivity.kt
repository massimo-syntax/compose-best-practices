package com.example.state_layout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.state_layout.ui.theme.State_LayoutTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            State_LayoutTheme {
                // OnGloballyPositioned()
                // CenterItemOnScroll()
                // ScopedRecomposition()
                SmartComposition()
            }
        }
    }
}

