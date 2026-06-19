package com.example.custombottomsheet

import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun AdvancedPeekHeightSheetScreen(){



    AdvancedPeekHeightSheet(
        100.dp,
        500.dp
    ){
        Text("custom bottom sheet")
    }

}


@Composable
fun AdvancedPeekHeightSheet(
    peekHeight: Dp,
    fullHeight: Dp,
    content: @Composable () -> Unit
) {
    var offset by remember { mutableFloatStateOf(100f) }
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(0, offset.roundToInt()) }
            .background(Color.Green)
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    offset = (offset + delta).coerceIn(
                        minimumValue = 0f,
                        maximumValue = with(density) { fullHeight.toPx() }
                    )
                }
            )
    ) {
        content()
    }
}