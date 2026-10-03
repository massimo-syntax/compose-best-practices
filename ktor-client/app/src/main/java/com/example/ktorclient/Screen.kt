package com.example.ktorclient

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ktorclient.client.KtorClient
import com.example.ktorclient.model.Post
import kotlinx.coroutines.launch

@Composable
fun Screen(){

    var posts by remember { mutableStateOf(emptyList<Post>()) }

    val scope = rememberCoroutineScope()
    val ktorClient = KtorClient()

    Column(
        Modifier.statusBarsPadding()
    ) {
        Text("Ktor http request ${posts.size}")
        Button({
            scope.launch {
                posts = listOf(Post(title = "loading"))
                posts = ktorClient.getPosts()
            }
        }){
            Text("Get posts")
        }
        MainContentList(list = posts)
    }
}


@Composable
fun MainContentList(
    modifier: Modifier = Modifier,
    list: List<Post>,
){
    LazyColumn(modifier.fillMaxSize()) {
        items(list){
            Column(modifier.padding(8.dp)) {
                Row{
                    Text("${it.id}", color = Color.Blue)
                    Text(it.title, color = Color.LightGray)
                }
                Text(it.body)
                HorizontalDivider()
            }
        }
    }
}
