package com.example.state_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp


@Composable
fun LayoutModifyer() {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

//        With **Modifier.layout{}** you can increase content size bigger than parent.
//        Red background contains three Boxes, second Box size is increased by
//        40.dp and it's position is offset to left by 20.dp
        LayoutModifierSample2()


//        layout order is from bottom to top but Constraints come from top to bottom
//        and disregarded or adjusted to min or max of existing Constraints
//        when it's not in bounds.
        MultipleLayoutsModifier()

    }
}



@Composable
private fun LayoutModifierSample2() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp), clip = false)
            .background(getRandomColor())
    ) {

        // some -bar-
        // same width as parent
        // 20 dp height
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                .background(getRandomColor())
        )

        Spacer(modifier = Modifier.height(16.dp))

        // We increase dimensions of content by 40.dp
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .border(2.dp, Color.Yellow)
                .layout { measurable: Measurable, constraints: Constraints ->
                    // from parent constraints,
                    // have min- max- width 40 dp more
                    val placeable = measurable.measure(
                        constraints.copy(
                            minWidth = constraints.maxWidth + 40.dp.roundToPx(),
                            maxWidth = constraints.maxWidth + 40.dp.roundToPx()
                        )
                    )
                    // be sure that the container does not expand
                    val layoutWidth =
                        placeable.width.coerceIn(constraints.maxWidth, constraints.maxWidth)

                    // nor in height
                    val layoutHeight =
                        placeable.height.coerceIn(constraints.minHeight, constraints.maxHeight)

                    layout(layoutWidth, layoutHeight) {
                        //          [++]        - [++++] =   [--[ / 2 = [-[++]
                        val xPos = (layoutWidth - placeable.width) / 2
                        //                          [-[++]+]
                        placeable.placeRelative(xPos, 0)
                    }
                }
                .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                .background(getRandomColor())
        )
        Spacer(modifier = Modifier.height(16.dp))
        // another -bar-
        // same width as parent
        // 20 dp height
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .shadow(4.dp, shape = RoundedCornerShape(8.dp))
                .background(getRandomColor())
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun MultipleLayoutsModifier() {
    // Also change placement position to show it affects Modifiers or
    // Constraints after Modifier.layout

    /*
        Prints:
        I  🍎 Bottom Measurement phase  minWidth: 180.0.dp, maxWidth: 180.0.dp, placeable width: 180.0.dp
        I  🍏 Middle Measurement phase minWidth: 100.0.dp, maxWidth: 300.0.dp, placeable width: 180.0.dp
        I  🌻Top Measurement phase minWidth: 0.0.dp, maxWidth: 392.72726.dp, placeable width: 300.0.dp
        I  🌻🌻 Top Placement Phase
        I  🍏🍏 Middle Placement Phase
        I  🍎🍎 Bottom Placement Phase

     */
    BoxWithConstraints(
        modifier = Modifier
            .height(300.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp), clip = false)
            .background(getRandomColor())
            // parent is .height(300.dp), no width constraints, then width = screen-width
            .layout { measurable, constraints ->
                // same constraints as the parent
                // now the constraints here remain in the Modifier
                // so the next layout{}s , will have the constraints defined, (found) here
                val placeable = measurable.measure(constraints)
                println(
                    "🌻Top Measurement phase " +
                            "minWidth: ${constraints.minWidth.toDp()}, " +
                            "maxWidth: ${constraints.maxWidth.toDp()}, " +
                            "placeable width: ${placeable.width.toDp()}"
                )

                layout(constraints.maxWidth, constraints.maxHeight) {
                    println("🌻🌻 Top Placement Phase")
                    // +x
                    placeable.placeRelative(50, 0)
                }
            }
            // override modifiers for next layout
            .widthIn(min = 100.dp, max = 300.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp), clip = false)
            .background(getRandomColor())
            .layout { measurable, constraints ->

                // 🔥Measuring this Measurable with this Constraints
                // passes it to next LayoutModifier or LayoutModifierNode
                val placeable = measurable.measure(
                    constraints
                        .copy(
                            minWidth = 180.dp.roundToPx(),
                            maxWidth = 250.dp.roundToPx(),
                            minHeight = 180.dp.roundToPx(),
                            maxHeight = 250.dp.roundToPx()
                        )
                )
                println(
                    "🍏 Middle Measurement phase " +
                            "minWidth: ${constraints.minWidth.toDp()}, " +
                            "maxWidth: ${constraints.maxWidth.toDp()}, " +
                            "placeable width: ${placeable.width.toDp()}"
                )

                layout(constraints.maxWidth, constraints.maxHeight) {
                    println("🍏🍏 Middle Placement Phase")
                    // +y
                    placeable.placeRelative(0, 50)
                }
            }

            // example: Constraints minWidth = 100.dp, maxWidth = 100.dp is not
            // in bounds of Constraints that placeable measured above
            // Because it's smaller than minWidth, minWidth and maxWidth
            // is changed to 180.dp from layout above
          //.width(100.dp)
            // This Constraints minWidth = 240.dp, maxWidth = 240.dp is valid
            // for 180.dp-250.dp range
          //.size(240.dp)
            // .width(100.dp) just shrinks max width to 180, same as min width
            .shadow(4.dp, shape = RoundedCornerShape(8.dp), clip = false)
            .background(getRandomColor())
            .layout { measurable, constraints ->

                val placeable = measurable.measure(constraints)
                println(
                    "🍎 Bottom Measurement phase  " +
                            "minWidth: ${constraints.minWidth.toDp()}, " +
                            "maxWidth: ${constraints.maxWidth.toDp()}, " +
                            "placeable width: ${placeable.width.toDp()}"
                )
                layout(placeable.width, placeable.height) {
                    println("🍎🍎 Bottom Placement Phase")
                    // +x +y
                    placeable.placeRelative(150, 150)
                }
            }
            .shadow(4.dp, shape = RoundedCornerShape(8.dp), clip = false)
            .background(getRandomColor()),
            // this as well would just shrink max width
            // so text has constraints of minWIdth, not less
            // hence doing so does nothing
            //.width(50.dp),

        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Min width: $minWidth\n" +   // 180
                    "maxWidth: $maxWidth",      // 2500
            modifier = Modifier
                .border(2.dp, Color.Red)
                .padding(5.dp)
                // this works as expected
                .width(100.dp),
                // but overrides the width of the smaller layouts above
                // so the text says 180 - 250, but (min) it is as given here
                // the layouts more than 100 are not shrinking to 100
            color = Color.White
        )
        // compose measures this text,
        // then updates the min width for all layouts in the parent
        // so that they would fit the text
    }
}