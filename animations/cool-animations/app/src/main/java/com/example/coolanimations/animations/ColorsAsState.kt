package com.example.coolanimations.animations

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp



@Composable
fun ColorsAsState(){

    val COLOR_TRANSITION_MS = 600 // Tweak how slow/fast colors morph
    // Edit palette colors live to swap the theme.
    val PALETTE = listOf(
        Color(0xFFFF7043) to "Red",
        Color(0xFFFFE600) to "Yellow",
        Color(0xFF3EC2FD) to "LightBlue",
        Color(0xFF47CD72) to "Green",
    )
    val COLORED_SHOWCASE_HEIGHT_DP = 180 // Make the hero box taller/shorter

    var selectedIndex by remember { mutableIntStateOf(0) }
    // coerceIn is a small guard that keeps the index inside the palette bounds even if the list shrinks during a hot reload
    val (selectedColor, selectedColorName) = PALETTE[selectedIndex.coerceIn(0, PALETTE.lastIndex)]

    //launches a coroutine on the recomposer that drives the value from the current animated color to the new target according to the spec
    val animatedBg by animateColorAsState(
        targetValue = selectedColor,
        animationSpec = tween(durationMillis = COLOR_TRANSITION_MS),
        label = "bg",
    )

    // animate the color of the text inside the showcase box
    val animatedContent by animateColorAsState(
        targetValue = if (selectedColor.luminance() > 0.5f) Color(0xFF202020) else Color.White,
        animationSpec = tween(durationMillis = COLOR_TRANSITION_MS),
        label = "textContent",
    )

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "animateColorAsState swatches",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        // main container that changes background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(COLORED_SHOWCASE_HEIGHT_DP.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(animatedBg),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = selectedColorName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = animatedContent,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PALETTE.forEachIndexed { index, (color, name) ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedIndex = index },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(color),
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }

}



