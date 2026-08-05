package com.example.state_layout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import com.example.state_layout.ui.theme.State_LayoutTheme
import kotlin.random.Random


fun getRandomColor() =  Color(
    red = Random.nextInt(256),
    green = Random.nextInt(256),
    blue = Random.nextInt(256),
    alpha = 255
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            State_LayoutTheme {
                // OnGloballyPositioned()
                // CenterItemOnScroll()
                // ScopedRecomposition()
                // SmartComposition()
                // LayoutModifyer()
                // LayoutPhases()
                // DeferState()
            }
        }
    }
}

