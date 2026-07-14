package com.example.splashscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random
@Composable
fun Screen() {
    val density = LocalDensity.current;
    val configuration = LocalConfiguration.current;
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.roundToPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.roundToPx() }
    var on by remember { mutableStateOf(true) }
    var x by remember { mutableIntStateOf(0) }
    var y by remember { mutableIntStateOf(0) }
    val fontSize: TextUnit = 8.sp
    val backgroundChar = '@'
    val overloadXThisChar = .57f
    val overloadYThisChar = .56f
    // how many chars fit the width - height
    val width = screenWidthPx / (fontSize.spToPx()) * overloadXThisChar
    val height = screenHeightPx / (fontSize.spToPx()) * overloadYThisChar
    fun createOneFrame(screen:List<MutableList<Char>>, sb:StringBuilder = StringBuilder()): String{
        for (h in 0 until height.toInt()) { // height
            for (i in 0 until width.toInt()) {
                sb.append(screen[h][i])
            }
            sb.append('\n')
        }
        return sb.toString()
    }
    var text by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while(on){
            // nice to also to keep the creation of this list out of Launched effect
            val screen =
                List(height.toInt()) {
                    MutableList(width.toInt()) { backgroundChar }
                }
            delay(400)
            val count = Random.nextInt(1000)
            repeat(count){
                val x = Random.nextInt(width.toInt())
                val y = Random.nextInt(height.toInt())
                screen[y][x] = '#'
            }
            //mustRefreshScreen()
            text = createOneFrame(screen,StringBuilder())
        }
    }
    AiScreen(
        text = text,
        textSize = fontSize
    ) { on = false }
}

@Composable
fun AiScreen(
    text: String = "",
    textSize: TextUnit,
    onStop: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.clickable { onStop() },
            style = TextStyle(
                fontSize = textSize,
                lineHeight = textSize,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}


