package com.example.state_layout


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.random.Random

fun getRandomColor() =  Color(
    red = Random.nextInt(256),
    green = Random.nextInt(256),
    blue = Random.nextInt(256),
    alpha = 255
)

@Composable
fun SmartComposition(){
    Column(
        Modifier
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        SimpleSample()
        DifferentLayers()
        RecomposingAlsoRootComposable()
    }
}

@Composable
private fun SimpleSample() {
    RandomColorColumn {
        var counter by remember { mutableIntStateOf(0) }
        RandomColorText("Recompose text in button, which reads -counter-")
        RandomColorButton(onClick = { counter++ }) {
            RandomColorText(text = "counter: $counter")
        }
    }
}

@Composable
private fun DifferentLayers() {
    RandomColorColumn {
        var update1 by remember { mutableIntStateOf(0) }
        RandomColorText("state is encapsulated in every button scope")
        RandomColorText("every reading of state happens inside own different composables scope")
        // button not recomposed
        RandomColorButton(onClick = { update1++ }) {
            // text yet
            RandomColorText(text = "Update1: $update1")
        }
        RandomColorColumn {
            var update2 by remember { mutableIntStateOf(0) }
            // button or prior components not recomposed
            RandomColorButton(onClick = { update2++ }) {
                // also here text is recomposed
                RandomColorText(text = "Update2: $update2")
            }
            // not recomposed
            RandomColorColumn {
                RandomColorText("inner not receiving state")
            }
        }
    }
}


@Composable
private fun RecomposingAlsoRootComposable() {
    RandomColorColumn {
        var update1 by remember { mutableIntStateOf(0) }
        var update2 by remember { mutableIntStateOf(0) }
        // not recomposed
        RandomColorText("text below is also recomposed, there is 1 reading of state in the same @Composable scope")
        // recomposed
        Text("im Text(\"\"); im an inline function ;)", color = getRandomColor())
        // not recomposed
        RandomColorButton(onClick = { update1++ }) {
            // that is recomposed, here happens recomposition.
            RandomColorText(text = "Update1: $update1")
        }
        // also
        RandomColorButton(
            onClick = { update2++ }
        ) {
            // either
            RandomColorText(text = "Update2: $update2")
        }
        // not recomposed
        RandomColorColumn {
            var update3 by remember { mutableIntStateOf(0) }
            // not recomposed
            RandomColorButton(onClick = { update3++ }) {
                // recomposed
                RandomColorText(text = "Update2: $update2, Update3: $update3")
            }
            // not
            RandomColorColumn {
                // yes, recomposed
                RandomColorText(text = "Update1: $update1")
                // no, no arguments
                SomeComposable()
            }
        }
        // this has his @Composable not inline function
        SomeComposable()
        // this gets recomposed when in the same layer a state is read
        // material design components are inline due to performance optimization
        Text(
            "⚠️ SomeComposable() below that reads update2 causes entire composable " +
                    "to be recomposed because it's at same level. " +
                    "Wrap it with RandomColorColumn to prevent this",
            color = getRandomColor()
        )
        // plain reading of state without outer non inline @Composable scope
        SomeComposable(update2)
    }
}



@Composable
private fun SomeComposable(update: Int = 0) {
    val text = if (update == 0) "no args" else "update: $update"
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(getRandomColor()),
        text = "SomeComposable $text",
        textAlign = TextAlign.Center,
        color = getRandomColor()
    )
}


@Composable
fun RandomColorColumn(content: @Composable ()->Unit){
    Column(
        Modifier
            .background(getRandomColor())
            .padding(8.dp)
    ) {
        content()
    }
}

@Composable
fun RandomColorText(text: String){
    Text(text, color = getRandomColor())
}

@Composable
fun RandomColorButton(onClick: ()->Unit, content: @Composable ()-> Unit){
    val mainButtonColor = ButtonDefaults.buttonColors(
        containerColor = getRandomColor(),
    )
    Button(onClick, colors = mainButtonColor) { content() }
}

@Composable
fun RandomColor (content: @Composable ()->Unit){
    content()
}
