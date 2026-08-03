package com.example.state_layout

import androidx.collection.objectFloatMap
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.let
import kotlin.random.Random


data class GloballyPositionedData(
    // same as position in root
    val positionInWindow: Offset,
    val boundsInWindow:Rect
)


data class dotOffset(
    val x: Float = 10f,
    val y: Float = 100f
)


@Composable
fun OnGloballyPositioned(){

    var positionedData by remember{
        mutableStateOf(
            GloballyPositionedData(
                Offset(0f,0f),
                Rect(0f,0f,0f,0f)
            )
        )
    }

    val scope = rememberCoroutineScope()


    var dotX by remember{ mutableFloatStateOf(0f) }
    var dotY by remember{ mutableFloatStateOf(0f) }

    var animateDotX = animateFloatAsState(dotX)
    var animateDotY = animateFloatAsState(dotY)

    var someOffsetTop by remember{ mutableFloatStateOf(0f) }


    // dot
    Box() {
        Surface(
            Modifier
                .size(8.dp)
                .graphicsLayer{
                    translationX = animateDotX.value
                    translationY = animateDotY.value
                },
            color = Color.Blue,
            shape = RoundedCornerShape(4.dp)
        ) { }
    }

    // container
    Column(
        Modifier.statusBarsPadding().fillMaxSize().border(1.dp, Color.Cyan),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("Parent..")
        Surface(
            Modifier.size(100.dp),
            border = BorderStroke(1.dp,Color.LightGray)
        ){}

        SelfGloballyPositionedInfo(
            someOffsetTop = someOffsetTop
        ){ offset, rect ->
            positionedData = GloballyPositionedData(offset,rect)
        }

        Spacer(Modifier.weight(1f))
        Button(
            {
                scope.launch{
                    someOffsetTop = Random.nextFloat() * 200
                    delay(500)
                    dotX = positionedData.positionInWindow.x
                    dotY = positionedData.positionInWindow.y
                }
            },
            modifier = Modifier.navigationBarsPadding()
        ){
            Text("click")
        }

    }




}



@Composable
private fun SelfGloballyPositionedInfo(
    someOffsetTop: Float,
    updatePositionedData: (Offset, Rect) -> Unit
) {
    val density = LocalDensity.current
    var text by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .padding(horizontal = 20.dp)
        .fillMaxWidth()
        .offset(0.dp, with(density){ someOffsetTop.toDp() } )
        .height(400.dp)
        .verticalScroll(rememberScrollState())
        .border(2.dp, Color.Red)
        .onGloballyPositioned {
            val positionInParent: Offset = it.positionInParent()
            val positionInRoot: Offset = it.positionInRoot()
            val positionInWindow: Offset = it.positionInWindow()
            val boundsInParent: Rect = it.boundsInParent()
            val boundsInRoot: Rect = it.boundsInRoot()
            val boundsInWindow: Rect = it.boundsInWindow()
            val parentCoordinates = it.parentCoordinates
            val parentLayoutCoordinates = it.parentLayoutCoordinates

            updatePositionedData(positionInWindow, boundsInWindow)

            text =
                "positionInParent: $positionInParent\n" +
                        "positionInRoot: $positionInRoot\n" +
                        "positionInWindow: $positionInWindow\n" +
                        "boundsInParent: $boundsInParent\n" +
                        "boundsInRoot: $boundsInRoot\n" +
                        "boundsInWindow: $boundsInWindow\n\n"

            // parent coordinates prints the same as position in parent
            parentCoordinates?.let { parent ->
                text +=
                    "parentCoordinates:\n" +
                            "positionInParent: ${parent.positionInParent()}\n" +
                            "positionInRoot: ${parent.positionInRoot()}\n" +
                            "positionInWindow: ${parent.positionInWindow()}\n\n"
            }

            parentLayoutCoordinates?.let { parent ->
                text +=
                    "parentLayoutCoordinates:\n" +
                            "positionInParent: ${parent.positionInParent()}\n" +
                            "positionInRoot: ${parent.positionInRoot()}\n" +
                            "positionInWindow: ${parent.positionInWindow()}"
            }
        }
    ) {
        Text(text = text)
    }
}