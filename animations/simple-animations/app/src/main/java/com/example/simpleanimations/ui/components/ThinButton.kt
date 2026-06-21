package com.example.simpleanimations.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simpleanimations.ui.theme.AccentGradient1
import com.example.simpleanimations.ui.theme.AccentGradient2

@Composable
fun ThinButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val corner = 16.dp

    Box(
        modifier = modifier
            .height(32.dp)
            .clip(RoundedCornerShape(corner))
            .background(Color.Transparent)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.35f),
                shape = RoundedCornerShape(corner)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(AccentGradient1.copy(alpha = 0.9f), AccentGradient2.copy(alpha = 0.9f))
                ),
                shape = RoundedCornerShape(corner)
            )
            .clickable(onClick = onClick),
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center).padding(8.dp),
            color = Color.White.copy(alpha = 0.95f),
            lineHeight = 12.sp,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
