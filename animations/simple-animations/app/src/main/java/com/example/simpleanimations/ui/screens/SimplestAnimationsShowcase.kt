package com.example.simpleanimations.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simpleanimations.ui.components.BOX_SIZE
import com.example.simpleanimations.ui.components.INNER_PADDING
import com.example.simpleanimations.ui.components.ListItemSquare
import com.example.simpleanimations.ui.components.ListItemWide
import com.example.simpleanimations.ui.components.ThinButton
import com.example.simpleanimations.ui.components.TitleGradient
import com.example.simpleanimations.ui.theme.AccentGradient2
import com.example.simpleanimations.ui.theme.CoolGray
import com.example.simpleanimations.ui.theme.Emerald
import com.example.simpleanimations.ui.theme.OceanBlue
import com.example.simpleanimations.ui.theme.Secondary
import com.example.simpleanimations.ui.theme.Teal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun SimplestAnimationsShowcase() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(24.dp))
        //          TITLE
        TitleGradient("Simplest Animations")
        // list
        Visibility()
        Alpha()
        Padding()
        Size() // not good
        Offset()
        OrderingList()
        GraphicLayerScale()
        AnimatedColor()
        SequentialAnimations()
        ConcurrentAnimations()
        TransitionExampleConcurrent()
        AnimatedContent()
    }
}

@Composable
fun Visibility() {
    Column {
        var visible by remember {
            mutableStateOf(true)
        }
        // Animated visibility will eventually remove the item from the composition once the animation has finished.
        AnimatedVisibility(visible) {
            ListItemWide("Visibility", "Animate visibility")
        }
        ThinButton(
            onClick = {
                visible = !visible
            },
            text = "Visibility"
        )
    }
}

@Composable
fun Alpha() {
    // [START android_compose_animation_cookbook_visibility_alpha]
    var visible by remember {
        mutableStateOf(true)
    }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1.0f else 0f,
        label = "alpha"
    )

    ListItemWide(
        "Alpha",
        "Animate alpha",
        Modifier.graphicsLayer { alpha = animatedAlpha }
    )
    ThinButton(
        onClick = { visible = !visible },
        text = "Animate alpha"
    )
    // [END android_compose_animation_cookbook_visibility_alpha]
}

@Composable
fun Padding() {
    var toggled by remember {
        mutableStateOf(false)
    }
    val animatedPadding by animateDpAsState(
        if (toggled) {
            20.dp
        } else {
            0.dp
        },
        label = "padding"
    )
    ListItemWide(
        "Animate Padding",
        "Tap to animate padding",
        Modifier
            .padding(animatedPadding)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                toggled = !toggled
            }
    )
}

@Composable
fun Size() {
    var expanded by remember { mutableStateOf(false) }

    val density = LocalDensity.current

    ListItemWide(
        "Animate size",
        "Tap to animate size",
        Modifier

            .animateContentSize(
                spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioHighBouncy
                )
            )
            //.animateContentSize()
            .height( if(expanded) BOX_SIZE + INNER_PADDING * 2 + 200.dp else BOX_SIZE + INNER_PADDING * 2 )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                expanded = !expanded
            }

    )
}

@Composable
fun Offset() {
    var moved by remember { mutableStateOf(false) }
    val pxToMove = with(LocalDensity.current) {
        50.dp.toPx().roundToInt()
    }

    val offset by animateIntOffsetAsState(
        targetValue = if (moved) {
            IntOffset(pxToMove, -pxToMove)
        } else {
            IntOffset.Zero
        },
        label = "offset",
        finishedListener = {}
    )

    ListItemWide(
        "Offset",
        "Tap to animate offset: $offset",
        Modifier
            .offset {
                offset
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                moved = !moved
            }
    )

}

@Composable
fun OrderingList(){
    val _data = (1..10).map { it.toString() }.toMutableList() // ["1","2",...,"10"]
    val displayedItems: MutableStateFlow<List<String>> = MutableStateFlow(_data)
    val data = displayedItems.collectAsState()

    Column {
        LazyRow {
            items(items = data.value, key = {it}){
                ListItemSquare(
                    text = it,
                    modifier = Modifier.animateItem()
                )
            }
        }
        ThinButton(
            onClick = {
              displayedItems.value = _data.shuffled()
            },
            text = "shuffle list"
        )
    }
}

@Composable
fun GraphicLayerScale(){
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = .5f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(5_000), RepeatMode.Reverse),
        label = "scale"
    )

    ListItemWide(
        "Graphic layer scale",
        "Infinite transition animation",
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin.Center
            }
    )
}

@Composable
fun AnimatedColor(){
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = OceanBlue,
        targetValue = Emerald,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "color"
    )
    Text(
        text = "Animated color",
        color = animatedColor,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SequentialAnimations() {
    val alphaAnimation = remember { Animatable(1f) }
    val yAnimation = remember { Animatable(0f) }

    /*
        LaunchedEffect("animationKey") {
            alphaAnimation.animateTo(1f)
            yAnimation.animateTo(100f)
            yAnimation.animateTo(500f, animationSpec = tween(100))
        }
        */
    val scope = rememberCoroutineScope()

    ListItemWide(
        "Sequential animation",
        "Animete: alpha, then y, then y again",
        modifier = Modifier
            .graphicsLayer {
                alpha = alphaAnimation.value
                translationY = -yAnimation.value
            }
    )

    ThinButton(
        text ="start sequential animation",
        onClick = {
            val alpha = Random.nextFloat()
            val y1 = Random.nextFloat() * 400
            val y2 = Random.nextFloat() * 600
            val ms = Random.nextInt(until = 1_500)
            scope.launch {
                alphaAnimation.animateTo(alpha, animationSpec = tween(500 + ms))
                yAnimation.animateTo(y1)
                yAnimation.animateTo(y2, animationSpec = tween(500 + ms))
            }
        }
    )

}

@Composable
fun ConcurrentAnimations() {
    val alphaAnimation = remember { Animatable(1f) }
    val yAnimation = remember { Animatable(0f) }
    val xAnimation = remember { Animatable(0f) }

    val scope = rememberCoroutineScope()

    ListItemWide(
        "Concurrent animation graphic layer",
        "Animate: alpha, y, x together",
        modifier = Modifier
            .graphicsLayer {
                alpha = alphaAnimation.value
                translationY = -yAnimation.value
                translationX = xAnimation.value
            }
    )

    ThinButton(
        text ="start concurrent animation",
        onClick = {
            val alpha = Random.nextFloat()
            val y = Random.nextFloat() * 400
            val x = Random.nextFloat() * 600
            val ms = Random.nextInt(until = 1_500)
            scope.launch {
                alphaAnimation.animateTo(alpha, animationSpec = tween(ms))
            }
            scope.launch {
                yAnimation.animateTo(y, animationSpec = tween(ms))
            }
            scope.launch {
                xAnimation.animateTo(x, animationSpec = tween(ms))
            }
        }
    )

}





enum class SomeState{
    Initial,
    Done
}

@Composable
fun TransitionExampleConcurrent() {

    var currentState by remember { mutableStateOf(SomeState.Initial) }
    val transition = updateTransition(currentState, label = "transition")

    val borderWidth by transition.animateDp(label = "borderWidth") { state ->
        when(state){
            SomeState.Initial -> 0.dp
            SomeState.Done -> 4.dp
        }
    }

    val color by transition.animateColor(label = "color") { state ->
        when(state){
            SomeState.Initial -> CoolGray
            SomeState.Done -> Teal
        }
    }

    Column(
        Modifier.padding(vertical = 8.dp)
    ){
        Text(
            text = "Animated border, color",
            color = color,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(borderWidth, Emerald)
        )
        ThinButton(
            text = "animate",
            onClick = {
                if(currentState == SomeState.Initial)
                    currentState = SomeState.Done
                else
                    currentState = SomeState.Initial
            }
        )
    }
}

enum class UiState{
    Loading,
    Loaded,
    Error
}

@Composable
fun AnimatedContent(){
    var state by remember {
        mutableStateOf(UiState.Loading)
    }

    Column{
        AnimatedContent(
            state,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(1000)
                ) togetherWith fadeOut(animationSpec = tween(1000))
            },
            label = "Animated Content"
        ) { targetState ->
            when (targetState) {
                UiState.Loading -> {
                    LoadingScreen()
                }
                UiState.Loaded -> {
                    LoadedScreen()
                }
                UiState.Error -> {
                    ErrorScreen()
                }
            }
        }
        Row{
            ThinButton(
                onClick = {
                    state = UiState.Loading
                },
                text = UiState.Loading.name
            )
            ThinButton(
                onClick = {
                    state = UiState.Loaded
                },
                text = UiState.Loaded.name
            )
            ThinButton(
                onClick = {
                    state = UiState.Error
                },
                text = UiState.Error.name
            )
        }
    }

}
@Composable
fun LoadingScreen(){

    val loading = arrayOf("Loading", "Loading.", "Loading..", "Loading...")
    var state by remember { mutableStateOf("Loading...") }


    LaunchedEffect(Unit){
        var i = 0
        while (true){
            delay(500.milliseconds)
            state = loading[i % loading.size]
            i++
        }
    }

    Text(
        text = state,
        color = Emerald,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun LoadedScreen(){
    ListItemWide(
        "Content Loaded!",
        "Content is in status: Loaded"
    )
}

@Composable
fun ErrorScreen(){

    Text(
        text = "ERROR",
        color = Secondary,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}