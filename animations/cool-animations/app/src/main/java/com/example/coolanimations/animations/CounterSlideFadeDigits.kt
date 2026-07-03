package com.example.coolanimations.animations


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CounterSlideFadeDigits(){
    val slideDurationMs = 250 // 120 (snappy) / 800 (luxurious)
    val fadeDurationMs = 450 // 0 (no fade) / 600 (long crossfade)
    val slideOffsetDivisor = 1 // 1 = full height slide, 2 = half, 4 = subtle

    // Add SizeTransform to AnimatedContent for animated width changes
    // e.g. AnimatedContent(... , transitionSpec = { ... using SizeTransform(...) })

    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            contentAlignment = Alignment.Center,
        ) {
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    // sliding direction
                    val goingUp = targetState > initialState
                    if (goingUp) {
                        // slide in + fade in with initial offset of [height] |_|
                        slideInVertically(
                            animationSpec = tween(slideDurationMs),
                            initialOffsetY = { it / slideOffsetDivisor },
                        )+fadeIn(animationSpec = tween(fadeDurationMs)) togetherWith
                                // together with
                                // slide out + fade out with target offset of [top] |^|
                                slideOutVertically(
                                    animationSpec = tween(slideDurationMs),
                                    targetOffsetY = { -it / slideOffsetDivisor },
                                ) + fadeOut(animationSpec = tween(fadeDurationMs)) using
                                // SizeTransform describes how the container box itself should resize when the new content has different measured dimensions.
                                // Without it, the container snaps to the new size on the first frame, which can clip the outgoing content.
                                SizeTransform { initialSize, targetSize -> tween(300) }
                    }else{
                        // offset is now over the top at beginning..
                        slideInVertically(
                            animationSpec = tween(slideDurationMs),
                            initialOffsetY = { -it / slideOffsetDivisor },
                        ) + fadeIn(animationSpec = tween(fadeDurationMs)) togetherWith
                                // and at bottom sliding away
                                slideOutVertically(
                                    animationSpec = tween(slideDurationMs),
                                    targetOffsetY = { it / slideOffsetDivisor },
                                ) + fadeOut(animationSpec = tween(fadeDurationMs)) using
                                // SizeTransform describes how the container box itself should resize when the new content has different measured dimensions.
                                // Without it, the container snaps to the new size on the first frame, which can clip the outgoing content.
                                SizeTransform { initialSize, targetSize -> tween(300) }
                    }

                },
                label = "counter",
            ) { value ->
                Text(text = "$value", fontSize = 96.sp, fontWeight = FontWeight.Bold)
            }
        }
        // commands
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        ) {
            Button(onClick = { count -= 1 }) {
                Text(text = "−", fontSize = 24.sp)
            }
            Button(onClick = { count += 1 }) {
                Text(text = "+", fontSize = 24.sp)
            }
        }

    }

}