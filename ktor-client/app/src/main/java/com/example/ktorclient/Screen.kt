package com.example.ktorclient

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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

    var result by remember { mutableStateOf(emptyList<Post>()) }

    val scope = rememberCoroutineScope()
    val ktorClient = KtorClient()

    Column(
        Modifier.statusBarsPadding()
    ) {
        Text("Ktor http request ${result.size}")
        // Buttons
        FlowRow{
            // GET
            Button({
                scope.launch {
                    result = listOf(Post(title = "loading"))
                    result = ktorClient.getPosts()
                }
            }){
                Text("Get posts")
            }
            // POST
            Button({
                scope.launch {
                    // show "loading"
                    result = listOf(Post(title = "loading"))
                    // create post
                    val post = Post(id = 123, title = "title", body = "new post", userId = 321)
                    // send post
                    val successfulPostResponse = ktorClient.postPost(post)
                    // update ui with the post sent to server
                    result = listOf(successfulPostResponse)
                }
            }){
                Text("Post post")
            }
            // PATCH
            Button({
                scope.launch {
                    // show "loading"
                    result = listOf(Post(title = "loading"))
                    // map of fields to update
                    val newPostFields = mapOf(
                        "title" to "hello",
                        "body" to "lorem ipsluu dllkls wi ao  si porro"
                    )
                    val id = 123
                    // send patch request
                    val successfulPatchResponse = ktorClient.patchPost(newPostFields, id)
                    // display updated post
                    result = listOf(successfulPatchResponse)
                }
            }){
                Text("Patch post")
            }
            // PUT
            Button({
                scope.launch {
                    // show "loading"
                    result = listOf(Post(title = "loading"))
                    // create updated post
                    val id = 0
                    val post = Post(id = id, title = "title", body = "new post", userId = 321)
                    // send post
                    val successfulPutResponse = ktorClient.putPost(post, id)
                    // update ui with the post sent to server
                    result = listOf(successfulPutResponse)
                }
            }){
                Text("Put post")
            }
            // DELETE
            Button({
                scope.launch {
                    // show "loading"
                    result = listOf(Post(title = "loading"))
                    val successfulDeleteRequest = ktorClient.deletePost(0)
                    val statusCode = successfulDeleteRequest.status.value
                    val statusCodeDescription = successfulDeleteRequest.status.description
                    // feedback to user
                    result = listOf(Post(
                        id = statusCode,
                        title = statusCodeDescription,
                    ))
                }
            }){
                Text("Delete post")
            }
            // query comments by url get parameter
            Button({
                scope.launch {
                    // show loading "state"
                    result = listOf(Post(title = "loading"))
                    val commentsResponse = ktorClient.commentsByIdQueryParameter(1)
                    val comments = commentsResponse.map{ comment ->
                        Post(
                            id = comment.id,
                            title = comment.email,
                            body = comment.body,
                        )
                    }
                    result = comments
                }
            }){
                Text("query parameter id comments")
            }
        } // end buttons
        // result in lazy column
        MainContentList(list = result)
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
