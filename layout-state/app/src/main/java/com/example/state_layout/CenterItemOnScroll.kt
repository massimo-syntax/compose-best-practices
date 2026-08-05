package com.example.state_layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex


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
                    indicator = { TabIndicator(tabState.ordinal) },

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
            .zIndex(-.1f)
            .layout { measurable: Measurable, constraints: Constraints ->
                // from parent constraints,
                // have min- max- width 40 dp more
                val placeable = measurable.measure(
                    constraints.copy(
                        minWidth = constraints.maxWidth + 20.dp.roundToPx(),
                        maxWidth = constraints.maxWidth + 20.dp.roundToPx()
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
            },
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
    val imageSizePx = with(density) { imageSize.toPx() }
    var placed by remember { mutableStateOf(false) }

    if (items.isEmpty()) {

        Column(
            Modifier
                .onPlaced { layoutCoordinates: LayoutCoordinates ->
                    val contentHeight = layoutCoordinates.size.height
                    val contentWidth = layoutCoordinates.size.width

                    // Secondary tab row + tab(s)
                    val parent = layoutCoordinates.parentLayoutCoordinates
                    // whole screen
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
                    placed = true
                }
                .offset {
                    IntOffset(offsetX.toInt(), offsetY.toInt())
                },

            ) {
            // or it jumps to the position after the 1st frames
            if(!placed) return@Column
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


