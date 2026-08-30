package com.example.sharedelementanimation.feature_list

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sharedelementanimation.core.navigation.DetailItem
import com.example.sharedelementanimation.core.navigation.itemsList

@Composable
fun ListScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onItemClick: (DetailItem) -> Unit
) {
    LazyColumn {
        itemsIndexed(itemsList) { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                with(sharedTransitionScope) {
                    Image(
                        painter = painterResource(id = item.image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .sharedElement(
                                sharedTransitionScope.rememberSharedContentState(key = "image-$index"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                    Text(
                        text = item.title,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .sharedElement(
                                sharedTransitionScope.rememberSharedContentState(key = "text-$index"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }
            }
        }
    }
}