package com.example.canvas.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CanvasSimple(){
    Column(
        Modifier.statusBarsPadding()
    ) {
        Text("thats going to be a canvas")
    }
}