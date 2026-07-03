package com.example.coolanimations.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun SwipeCardsOnDrag() {
    val SWIPE_THRESHOLD_FRACTION = 0.3f // 0.1 (easy) ↔ 0.6 (hard)
    val ROTATION_FACTOR = 0.15f // degrees per pixel
    val FLING_STIFFNESS = 300f // spring stiffness when snapping back
    val FLING_DURATION_MS = 300 // off screen exit duration
    val INITIAL_SCALE_VALUE = .55F

    val CARD_COLORS = listOf(
        Color(0xFFEF5350),
        Color(0xFF42A5F5),
        Color(0xFF66BB6A),
        Color(0xFFFFCA28),
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Swipe the top card left or right", modifier = Modifier.padding(horizontal = 8.dp))

        var topIndex by remember { mutableStateOf(0) }
        val offsetX = remember { Animatable(0f) }
        val animScale = remember { Animatable(INITIAL_SCALE_VALUE) }
        val scope = rememberCoroutineScope()

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp),
            contentAlignment = Alignment.Center,
        ) {
            val widthPx = with(LocalDensity.current) { maxWidth.toPx() }
            val threshold = widthPx * SWIPE_THRESHOLD_FRACTION

            if (topIndex >= CARD_COLORS.size) {
                Button(onClick = {
                    topIndex = 0
                    scope.launch { offsetX.snapTo(0f) }
                }) {
                    Text("All done. Tap to reset")
                }
            } else {
                val cardBehind = topIndex + 1
                if (cardBehind < CARD_COLORS.size) {
                    val scale = animScale.value
                    //val yOffset = (with(LocalDensity.current) { (300 * .03f).dp.toPx() }) / 2
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(300.dp)
                            //.offset { IntOffset(0, 0) }
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            },
                        colors = CardDefaults.cardColors(containerColor = CARD_COLORS[cardBehind]),
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                "Card ${cardBehind + 1}",
                                color = Color.White,
                                fontSize = 28.sp,
                                modifier = Modifier.graphicsLayer{
                                    scaleX = scale * 1.137f // sp do not scale well
                                    scaleY = scale * 1.137f
                                }
                            )
                        }
                    }
                }

                // Card on top
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(300.dp)
                        .offset { IntOffset(offsetX.value.toInt(), 0) }
                        .graphicsLayer { rotationZ = offsetX.value * ROTATION_FACTOR }
                        .pointerInput(
                            topIndex,
                            SWIPE_THRESHOLD_FRACTION,
                            FLING_STIFFNESS,
                            FLING_DURATION_MS
                        ) {
                            detectDragGestures(
                                onDrag = { change, drag ->
                                    change.consume()
                                    scope.launch { offsetX.snapTo(offsetX.value + drag.x) }
                                },
                                onDragEnd = {
                                    scope.launch {
                                        if (abs(offsetX.value) > threshold) {
                                            val target =
                                                if (offsetX.value > 0) widthPx * 1.5f else -widthPx * 1.5f
                                            offsetX.animateTo(
                                                target,
                                                animationSpec = tween(FLING_DURATION_MS)
                                            )
                                            // animate scale of the card behind
                                            animScale.animateTo(1f, tween(FLING_DURATION_MS))
                                            // then increase index
                                            topIndex += 1
                                            offsetX.snapTo(0f)
                                            animScale.snapTo(INITIAL_SCALE_VALUE)
                                        } else {
                                            offsetX.animateTo(
                                                0f,
                                                spring(stiffness = FLING_STIFFNESS)
                                            )
                                        }
                                    }
                                },
                            )
                        },
                    colors = CardDefaults.cardColors(containerColor = CARD_COLORS[topIndex]),
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Card ${topIndex + 1}", color = Color.White, fontSize = 32.sp)
                    }
                }
            }
        }
    }
}
