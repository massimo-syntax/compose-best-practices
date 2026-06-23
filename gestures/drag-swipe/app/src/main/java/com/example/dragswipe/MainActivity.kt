package com.example.dragswipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dragswipe.ui.screens.SwipeHorizontallyScreen
import com.example.dragswipe.ui.screens.VerticalDragAmountScreen
import com.example.dragswipe.ui.theme.DragSwipeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DragSwipeTheme {
                // VerticalDragAmountScreen()
                SwipeHorizontallyScreen()
            }
        }
    }
}
