package com.example.state_layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.collection.objectFloatMap
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.let
import kotlin.random.Random


enum class Tabs {
    Home, Products, Settings
}

@Composable
fun CenterItemOnScroll() {

    BoxWithConstraints {
        val screenHeight = maxHeight
        val scrollState = rememberScrollState()

        // whole scren
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState),
        ) {
            // header
            Header()
            var tabState by remember { mutableStateOf(Tabs.Home) }

            // tab row, tab content
            Column(
                Modifier.height(screenHeight)
            ) {
                SecondaryTabRow(
                    selectedTabIndex = tabState.ordinal,
                    indicator = { TabIndicator(tabState.ordinal) }
                ) {
                    Tabs.entries.forEach {
                        Tab(
                            selected = tabState.ordinal == it.ordinal,
                            onClick = { tabState = it },
                            text = {
                                Text(
                                    text = it.name,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                        )
                    }
                }

                when (tabState) {
                    Tabs.Home -> TabHome()
                    Tabs.Products -> TabProducts()
                    Tabs.Settings -> TabSettings()
                }

            }
        }
    }


}


// tab indicator
@Composable
fun TabIndicatorScope.TabIndicator(selectedTabIndex: Int) {
    Surface(
        Modifier
            .tabIndicatorOffset(selectedTabIndex, matchContentSize = true)
            .padding(vertical = 8.dp)
            .fillMaxHeight()
            .zIndex(-.1f),
        color = Color.LightGray,
        shape = RoundedCornerShape(10.dp),

        ) {}
}

// tabs
@Composable
fun TabHome() {
    val items: List<String> = emptyList()

    var offsetY by remember { mutableFloatStateOf(0f) }
    var offsetX by remember { mutableFloatStateOf(0f) }

    var text by remember { mutableStateOf("") }

    val imageSize = 120.dp
    val density = LocalDensity.current
    val imageSizePx = with(density){ imageSize.toPx() }

    if (items.isEmpty()) {

        Column(
            Modifier
                .onPlaced { layoutCoordinates: LayoutCoordinates ->
                    val contentHeight = layoutCoordinates.size.height
                    val contentWidth = layoutCoordinates.size.width

                    // This is Box inside ListLazyColumn
                    val parent = layoutCoordinates.parentLayoutCoordinates
                    // This is outer Column
                    val root = parent?.parentLayoutCoordinates?.parentLayoutCoordinates
                    if (parent != null && root != null) {

                        val rootHeight = root.size.height
                        val parentHeight = parent.size.height
                        val parentWidth = parent.size.width
                        val parentPosition = parent.positionInRoot().y

                        val tabHeight = rootHeight - parentHeight

                        offsetY =
                            ((parentHeight - rootHeight - tabHeight - parentPosition + contentHeight) / 2)

                        offsetX = (parentWidth / 2) - (imageSizePx / 2)

                        text =
                            "parent height: ${parentHeight}, \n" +
                                    "parent pos: $parentPosition \n" +
                                    "root height: ${rootHeight}, \n" +
                                    "contentHeight: $contentHeight, \n" +
                                    "tabHeight: $tabHeight, \n" +
                                    "offsetY: $offsetY"

                    }

                }
                .offset {
                    IntOffset(offsetX.toInt(), offsetY.toInt())
                },

        ) {

            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "empty state",
                modifier = Modifier.size(imageSize),
                colorFilter = ColorFilter.tint(Color.Blue)
            )
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 10.sp,
                ),
            )
        }
    } else {
    }// lazyColumn or else
}

@Composable
fun TabProducts() {
    val items: List<String> = emptyList()

    if (items.isEmpty()) {

        Text("Tab Products")


    } else {
    } // lazyColumn or else
}

@Composable
fun TabSettings() {
    val items: List<String> = emptyList()

    if (items.isEmpty()) {


        Text("Tab Settings")

    } else {
    } // lazyColumn or else
}

@Composable
fun Header() {
    Column(
        Modifier.statusBarsPadding()
    ) {
        // header image
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(Color.LightGray),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }
}


