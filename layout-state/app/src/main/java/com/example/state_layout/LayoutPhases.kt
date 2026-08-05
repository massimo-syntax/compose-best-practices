package com.example.state_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LayoutPhases() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
/*
            **Modifier.offset{}** defers reading state " +
            "from **Composition** to **Layout**",
 */
        Column {
            OffsetModifier()
        }

        // **Modifier.drawBehind{}** defers reading state to **Draw**


        Column {
            DrawWithContent()
        }
    }
}

@Composable
private fun OffsetModifier() {

    // offset x change effects also drawing of the next composables
    // not the composition
    var offsetY by remember { mutableFloatStateOf(0f) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "OffsetX")
        Spacer(modifier = Modifier.width(5.dp))
        // set, read state
        Slider(value = offsetY,
            valueRange = 0f..50f,
            onValueChange = {
                offsetY = it
            }
        )
    }

    val modifier1 = Modifier
        // Reads value directly
        .offset(y = offsetY.dp)
        // [.layout] is not really needed
        .layout { measurable, constraints ->
            val placeable: Placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) {
                // instead o 0 would add on offset
                placeable.placeRelative(0, 0)
            }
        }
        .background(getRandomColor())
        .drawWithContent {
            drawContent()
        }

    val modifier2 = Modifier
        // Deferring state to Layout phase prevents
        // Composables that have this modifier to be recomposed
        .offset {
            val newY = offsetY.dp.roundToPx()
            IntOffset(0, newY)
        }
        // [.layout] is not really needed
        .layout { measurable, constraints ->
            val placeable: Placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) {
                placeable.placeRelative(0, 0)
            }
        }
        .background(getRandomColor())
        .drawWithContent {
            drawContent()
        }
    // reading value in offset, the modifiers are given new.
    // the text background is everytime different
    MyBox(modifier = modifier1, "modifier1")
    MyBox(modifier = modifier2, "modifier2")
}

@Composable
private fun DrawWithContent() {

    var someValue by remember { mutableFloatStateOf(0f) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "someValue")
        Spacer(modifier = Modifier.width(5.dp))

        Slider(value = someValue,
            valueRange = 0f..50f,
            onValueChange = {
                someValue = it
            }
        )
    }

    // reading state in the modifier would create every time a new Modifier

    val modifier3 = Modifier
        // layout is not really needed here
        .layout { measurable, constraints ->
            val placeable: Placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) {
                placeable.placeRelative(0, 0)
            }
        }
        // 🔥 deferring color read in lambda only calls Draw and skips Composition and Layout
        .drawWithContent {
            // this drwas let say a "background"
            drawRect(getRandomColor())
            // without this, nothing is displayed
            // meaning: MyBox(){Column(Text(...)..} , nothing
            drawContent()
        }

    val modifier4 = Modifier
        // without layout works anyway
        .layout { measurable, constraints ->
            val placeable: Placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) {
                placeable.placeRelative(0, 0)
            }
        }
        .drawWithContent {
            println("🎾 modifier4 DRAW")
            drawRect(getRandomColor())
            drawContent()
        }
    // that does NOT inflict the text background
    // just redraws the Rect() -"background"-
    MyBox(modifier3, "modifier3")
    MyBox(modifier4, "modifier4")
}

@Composable
private fun MyBox(modifier: Modifier, title: String) {

    Column(modifier) {
        // This Text changes background in every recomposition
        Text(
            text = title,
            modifier = Modifier
                .background(getRandomColor())
                .fillMaxWidth()
                .padding(2.dp)
        )
        Text(
            text = "modifier hash: ${modifier.hashCode()}\n"
                    + "Modifier: $modifier",
            color = Color.White,
            //modifier = Modifier.heightIn(max = 120.dp),
            fontSize = 10.sp
        )
    }
}