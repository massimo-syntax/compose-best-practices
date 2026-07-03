package com.example.coolanimations.animations

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ColorPlusShape(){

    val springStiffness = Spring.StiffnessMediumLow
    val springDamping = Spring.DampingRatioMediumBouncy


    val collapsedSize = 74.dp
    val expandedSize = 196.dp
    val collapsedRotation = 0f
    val expandedRotation = 135f
    val expandedCornerDp = 24.dp

    var morphed by remember { mutableStateOf(false) }

    // overall size
    val size by animateDpAsState(
        targetValue = if (morphed) expandedSize else collapsedSize,
        animationSpec = spring(dampingRatio = springDamping, stiffness = springStiffness),
        label = "size",
    )


    // Driving multiple properties from one state
    // observing "morphed" boolean state
    // all those have target value triggered with the same boolean
    val rotation by animateFloatAsState(
        targetValue = if (morphed) expandedRotation else collapsedRotation,
        animationSpec = spring(dampingRatio = springDamping, stiffness = springStiffness),
        label = "rotation",
    )
    val cornerDp by animateDpAsState(
        targetValue = if (morphed) expandedCornerDp else collapsedSize / 2,
        animationSpec = spring(dampingRatio = springDamping, stiffness = springStiffness),
        label = "corner",
    )
    val color by animateColorAsState(
        targetValue = if (morphed) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
        animationSpec = spring(dampingRatio = springDamping, stiffness = springStiffness),
        label = "color",
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(size)
                    .rotate(rotation)
                    .clip(RoundedCornerShape(cornerDp))
                    .background(color = color, shape = RoundedCornerShape(cornerDp))
                    .clickable { morphed = !morphed },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

    }


}