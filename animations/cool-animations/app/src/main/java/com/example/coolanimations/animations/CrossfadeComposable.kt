package com.example.coolanimations.animations


import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CrossfadeComposable() {
    val crossfadeDurationMs = 1000

    val color1 = Color(0xFFFFB74D)
    val color2 = Color(0xFF4FC3F7)
    val color3 = Color(0xFF5C6BC0)

    val tabs = listOf(
        Triple("🌅", "Morning", color1),
        Triple("🌞", "Noon", color2),
        Triple("🌙", "Night", color3),
    )

    var selected by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {


        Crossfade(
            targetState = selected,
            animationSpec = tween(crossfadeDurationMs),
            label = "tab-crossfade",
        ) { current ->
            val (emoji, name, color) = tabs[current]
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                shape = RoundedCornerShape(20.dp),
                color = color,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(text = emoji, fontSize = 96.sp)
                        Text(
                            text = name,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                }
            }
        }
        // commands
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            tabs.forEachIndexed { index, (emoji, name, _) ->
                val isSelected = index == selected
                if (isSelected) {
                    Box{
                        Button(
                            onClick = { selected = index },
                            modifier = Modifier.height(44.dp),
                        ) {
                            Text(text = "$name", fontSize = 14.sp)
                        }
                        Text(text = "$emoji", fontSize = 14.sp)
                    }

                } else {
                    Box{
                        OutlinedButton(
                            onClick = { selected = index },
                            modifier = Modifier.height(44.dp),
                            colors = ButtonDefaults.outlinedButtonColors(),
                        ) {
                            Text(text = "$name", fontSize = 14.sp)
                        }
                        Text(text = "$emoji", fontSize = 14.sp)
                    }

                }
            }
        }
    }
}