package com.example.dragswipe.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


@Composable
fun VerticalDragAmountScreen(){

    // this is from
    // https://developer.android.com/develop/ui/compose/touch-input/pointer-input/drag-swipe-fling
    // not used here
    // var offsetX by remember { mutableFloatStateOf(0f) }

    var amountDragY by remember { mutableFloatStateOf(0f) }

    // Get local density from composable
    val density: Density = LocalDensity.current

    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics

    // Device density
    //val densityFloat: Float = displayMetrics.density

    // Screen height px , dp
    val screenHeightPx: Int = displayMetrics.heightPixels
    val screenHeightDp: Int = with(density){screenHeightPx}

    // Create element height in pixel state
    var columnHeightPx by remember { mutableFloatStateOf(0f) }
    // Create element height in dp state
    var columnHeightDp by remember { mutableStateOf(0.dp) }

    val columnOffsetY = screenHeightPx - columnHeightPx

    // surface to whole screen size
    Box(Modifier.height(screenHeightDp.dp).border(1.dp, Color.Blue)) {

        // smaller column with draggable item
        Column(
            Modifier.border(1.dp, Color.Black)
                .onGloballyPositioned { coordinates ->
                    // Set column height using the LayoutCoordinates
                    columnHeightPx = coordinates.size.height.toFloat()
                    columnHeightDp = with(density) { coordinates.size.height.toDp() }
                }
                .offset {
                    IntOffset(
                        0,
                        columnOffsetY.roundToInt() + amountDragY.roundToInt()
                    )
                }


        ) {
            Text("drag amount")
            //Text("$offsetX")
            Text("$amountDragY")
            Text("screen height: $screenHeightPx")
            Text("column height: $columnHeightPx")
            Text("column offset y: $columnOffsetY")

            // draggable item
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .border(1.dp, Color.Red)
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            amountDragY += delta
                        }
                    )
            ){
                Text(
                    text ="drag here",
                    modifier = Modifier.padding(16.dp)
                )
            }


        }
    }



}