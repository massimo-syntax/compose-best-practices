package com.example.sharedelementanimation.feture_detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sharedelementanimation.core.navigation.itemsList

@Composable
fun DetailScreen(
    item: ItemDetailKey,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackPressed: () -> Unit
) {
    val index = itemsList.indexOfFirst { it.id == item.id }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBackPressed) {
            Text("Back")
        }
        with(sharedTransitionScope) {
            Image(
                painter = painterResource(id = item.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "image-$index"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
            Text(
                text = item.title,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "text-$index"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
        }
    }
}