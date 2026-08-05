package com.example.state_layout

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp


@Composable
fun DeferState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
//      In this example value is passed directly,
//      even  if **Outer** and **Middle**
//      composables don't read the value they are recomposed
        NonDeferredComposablesSample()

//      In this example state is deferred until **InnerDeferred** composable reads.
//      Because of that Composables between don't get recomposed
        DeferredComposablesSample()

//      In this example state is deferred until **InnerDeferred** composable reads the value. Even if there is no **Modifier.padding{}**
//      with lambda we send lambda to inner composable
//      to make sure only inner Composable is recomposed
        DeferredPaddingComposablesSample()
    }
}

@Composable
private fun NonDeferredComposablesSample() {
    var offsetX by remember { mutableFloatStateOf(0f) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Non deferred")
        Spacer(modifier = Modifier.width(5.dp))
        Slider(value = offsetX,
            valueRange = 0f..50f,
            onValueChange = {
                offsetX = it
            }
        )
    }

    Outer(offsetX.toInt())
}

@Composable
private fun DeferredComposablesSample() {
    var offsetX by remember { mutableFloatStateOf(0f) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Deferred state")
        Spacer(modifier = Modifier.width(5.dp))
        Slider(value = offsetX,
            valueRange = 0f..50f,
            onValueChange = {
                offsetX = it
            }
        )
    }

    OuterDeferred {
        offsetX.toInt()
    }
}

@Composable
private fun DeferredPaddingComposablesSample() {
    var padding by remember { mutableFloatStateOf(0f) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Deferred Padding")
        Spacer(modifier = Modifier.width(5.dp))
        Slider(value = padding,
            valueRange = 0f..50f,
            onValueChange = {
                padding = it
            }
        )

    }
    PaddingOuterDeferred {
        padding.dp
    }
}


//  @ STATE IS DEFERRED
@Composable
private fun OuterDeferred(offset: () -> Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(getRandomColor())
            .padding(10.dp)
    ) {
        Text("OuterDeferred")
        MiddleDeferred(offset)
    }
}

@Composable
private fun MiddleDeferred(offset: () -> Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(getRandomColor())
            .padding(10.dp)
    ) {
        Text("MiddleDeferred")
        InnerDeferred(offset)
    }
}

@Composable
private fun InnerDeferred(offset: () -> Int) {

    Text(
        text = "InnerDeferred",
        modifier = Modifier
            .fillMaxWidth()
            .background(getRandomColor())
            .offset {
                IntOffset(x = offset().dp.roundToPx(), 0)
            }
    )
}



//  @ NOT DEFERRED
@Composable
private fun Outer(offset: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(getRandomColor())
            .padding(10.dp)
    ) {
        Text("Outer")
        Middle(offset)
    }
}

@Composable
private fun Middle(offset: Int) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(getRandomColor())
            .padding(10.dp)
    ) {
        Text("Middle")
        Inner(offset)
    }
}

@Composable
private fun Inner(offset: Int) {
    Text(
        text = "Inner",
        modifier = Modifier
            .fillMaxWidth()
            .background(getRandomColor())
            .offset {
                IntOffset(x = offset.dp.roundToPx(), 0)
            }
    )
}

//  @ PADDING DEFERRED
@Composable
private fun PaddingOuterDeferred(padding: () -> Dp) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(getRandomColor())
            .padding(10.dp)
    ) {
        Text("PaddingOuterDeferred")
        PaddingMiddleDeferred(padding)
    }
}

@Composable
private fun PaddingMiddleDeferred(padding: () -> Dp) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(getRandomColor())
            .padding(10.dp)
    ) {
        Text("PaddingMiddleDeferred")
        PaddingInnerDeferred(padding)
    }
}

@Composable
private fun PaddingInnerDeferred(padding: () -> Dp) {

    Text(
        text = "PaddingInnerDeferred",
        modifier = Modifier
            .padding(start = padding())
            .fillMaxWidth()
            .background(getRandomColor())

    )
}