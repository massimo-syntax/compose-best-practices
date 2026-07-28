package com.example.custombottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SlideCustomColumnScreen(){

    val containerSize = LocalWindowInfo.current.containerSize
    val density = LocalDensity.current.density

    val screenHeightDp = containerSize.height / density
    val screenHeightPx = containerSize.height

    val screenWidth = containerSize.width
    val screenWidthDp = containerSize.width / density


    Surface(
        Modifier.width(screenWidthDp.dp).height(screenHeightDp.dp),
        color = Color.LightGray
    ){}

    val boxHeight = 200.dp
    val boxHeightPx = with(LocalDensity.current){boxHeight.toPx()}
    Box(
        Modifier
            .fillMaxWidth()
            .border(1.dp,Color.Blue)
            .height(boxHeight)
    ){}

    data class Drag(
        val offset: Float,
        val delta: Float
    )

    var dragState by remember{ mutableStateOf(Drag(0f,0f)) }

    // this example is that much low level that we dont use lambda..
    // we dont have to return nothing, for now
    fun onDrag(offset: Float, delta: Float){
        dragState = Drag(offset, delta)
    }

    AdvancedPeekHeightSheet(
        offsetFullHeight = boxHeightPx,
        offsetPeekHeight = screenHeightPx - 100f,
        onDrag = ::onDrag
    ){
        Column{
            Text("screenHeightPx: $screenHeightPx, screenHeightDp: $screenHeightDp")
            Text("boxHeightPx: $boxHeightPx, boxHeightDp: $boxHeight")
            Text("drag offset: ${dragState.offset} \ndrag delta: ${dragState.delta}")
            Text("container size: $containerSize, density: $density")
        }

    }

}


@Composable
fun AdvancedPeekHeightSheet(
    offsetFullHeight: Float,
    offsetPeekHeight: Float,
    onDrag: (Float, Float) -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current

    var offset by remember {
        mutableFloatStateOf(
            offsetPeekHeight
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(0, offset.roundToInt()) }
            .background(Color.Green)
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    offset = (offset + delta).coerceIn(
                        maximumValue = offsetPeekHeight,
                        minimumValue = offsetFullHeight
                    )
                    onDrag(offset,delta)
                }
            )
    ) {
        content()
    }
}