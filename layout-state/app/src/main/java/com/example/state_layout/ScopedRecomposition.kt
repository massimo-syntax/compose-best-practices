package com.example.state_layout

import androidx.compose.runtime.Composable
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ScopedRecomposition(){
    Column(Modifier
        .statusBarsPadding()
        .scrollable(
            rememberScrollState(),
            orientation = Orientation.Vertical

        )
    ) {
        // this does not work..
        // NeverEqualPolicy()

        // that works the same also without
        // StructuralEqualitySample()

        // that is cool ;)
        RootComposable()
    }
}


@Composable
private fun RootComposable() {
    var counter by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    Toast.makeText(context, "[0->]composition: RootComposable()",Toast.LENGTH_SHORT).show()

    Column(
        modifier = Modifier
            .padding(4.dp)
            .shadow(1.dp, shape = CutCornerShape(topEnd = 8.dp))
            .padding(4.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { counter++ }) {
            Toast.makeText(context, "[0,5->] composition: Button()-same level as root-",Toast.LENGTH_SHORT).show()
            Text(text = "counter++"+counter)
        }
        // without to read the state value here,
        // Root() never gets recomposed
        // Text("$counter")

        // this, same as RootComposable is composed 1nce
        OuterComposable {
            // This scope never gets recomposed because
            // Nothing is read here
            Text(text = "Outer Composable")
            MiddleComposable {
                // InnerComposable is recomposed (obviously)
                InnerComposable(text = "Counter $counter")
            }
        }
    }
}

@Composable
fun OuterComposable(content: @Composable ()->Unit){
    val context = LocalContext.current
    Toast.makeText(context, "[1->]composition: OuterComposable()",Toast.LENGTH_SHORT).show()
    content()
}

@Composable
fun MiddleComposable(content: @Composable ()->Unit){
    val context = LocalContext.current
    Toast.makeText(context, "[2->]composition: MiddleComposable()",Toast.LENGTH_SHORT).show()
    Text(" MiddleComposable()")
    content()
}

@Composable
fun InnerComposable(text: String){
    val context = LocalContext.current
    Toast.makeText(context, "[3->]composition: InnerComposable()",Toast.LENGTH_SHORT).show()
    Text("  Inner "+text)
    InnerInnerComposable()
}

@Composable
fun InnerInnerComposable(){
    val context = LocalContext.current
    Toast.makeText(context, "[4->]composition: InnerInnerComposable()",Toast.LENGTH_SHORT).show()
    Text("   InnerInnerComposable")
}





// WARNING, DEPRECATED FUNCTIONALITY
// (tested on real device API level 30, the emulator is 35, fair enough)

// this does not work
@Composable
private fun NeverEqualPolicy() {
    var counter by remember {
        mutableStateOf(
            value = 0,
            // 🔥Setting policy changes whether recomposition should
            // be triggered when same value is set in this example
//            policy = referentialEqualityPolicy(),
//            policy = structuralEqualityPolicy(),
            policy = neverEqualPolicy()
        )
    }
    val context = LocalContext.current
    Toast.makeText(context, "recomposed", Toast.LENGTH_SHORT).show()
        Button(
            modifier = Modifier.fillMaxWidth(),
            // Depending on which policy is used setting same value will trigger recomposition
            onClick = { counter = 1 }) {
            SideEffect {
                Toast.makeText(context, "recomposing with counter: $counter", Toast.LENGTH_SHORT).show()
            }
            Text(text = "never equal policy: $counter")
        }
}

// that works the same
@Composable
private fun StructuralEqualitySample() {
    val context = LocalContext.current
    var someEventData by remember {
        mutableStateOf(
            value = SomeEventData(message = "structuralEqualityPolicy message"),
            // 🔥 For recomposition to be triggered we need to assign an object with
            // different message since data class checks primary constructor values
            // for equals function
            // policy = structuralEqualityPolicy()
        )
    }
    // This is for showing toast message only on each recomposition
    Toast.makeText(context, someEventData.message, Toast.LENGTH_SHORT).show()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            onClick = {
                someEventData =
                    someEventData.copy(message = "structuralEqualityPolicy message")
            }
        ) {
            Text(text = "Click to create Event")
        }
        Text(
            modifier = Modifier
                .border(2.dp, Color.Cyan)
                .fillMaxWidth()
                .padding(10.dp),
            text = someEventData.message,
            fontSize = 16.sp
        )
    }
}
data class SomeEventData(val message: String)
