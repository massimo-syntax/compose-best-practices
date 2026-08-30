package com.example.sharedelementanimation.core.navigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import com.example.sharedelementanimation.feature_list.ListKey
import com.example.sharedelementanimation.feature_list.ListScreen
import com.example.sharedelementanimation.feture_detail.DetailScreen
import com.example.sharedelementanimation.feture_detail.ItemDetailKey
import com.example.sharedelementanimation.R

data class DetailItem(
    val id: Int,
    val title: String,
    @DrawableRes val image: Int
)

val itemsList = listOf(
    DetailItem(0, "title 0", R.drawable.cpu),
    DetailItem(1, "title 1", R.drawable.cpu),
    DetailItem(2, "title 2", R.drawable.cpu),
    DetailItem(3, "title 3", R.drawable.cpu),
    DetailItem(4, "title 4", R.drawable.cpu),
    DetailItem(5, "title 5", R.drawable.cpu),
)


@Composable
fun Nav3SharedTransition() {
    SharedTransitionLayout {
        val backStack = rememberNavBackStack(ListKey)

        // Note: NavDisplay accepts a `sharedTransitionScope` parameter, which is used to animate
        // NavEntry instances between scenes. This parameter *isn't* required for shared element
        // or shared bounds transitioning elements between different NavEntry, as demonstrated in
        // this sample.
        // See https://developer.android.com/guide/navigation/navigation-3/animate-destinations#transition-nav-entries
        NavDisplay(
            modifier = Modifier.safeDrawingPadding(),
            backStack = backStack,
            entryProvider = entryProvider {
                entry<ListKey> {
                    ListScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        onItemClick = { item ->
                            backStack.add(ItemDetailKey(item.id, item.title, item.image))
                        }
                    )
                }
                entry<ItemDetailKey> { detailItem ->
                    DetailScreen(
                        item = detailItem,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        onBackPressed = {
                            backStack.removeLastOrNull()
                        },
                    )
                }
            })
    }
}