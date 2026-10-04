package com.example.ktorclient.client

import com.example.ktorclient.model.Comment
import com.example.ktorclient.model.Post
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {

    private val client = httpClient()

    suspend fun getPosts(): List<Post> =
        client
            .get("posts")
            .body<List<Post>>()

    suspend fun postPost(post: Post) =
        client.post{
            url{
                path("/posts")
            }
            contentType(ContentType.Application.Json)
            setBody(post)
        // this API returns the same post
        }.body<Post>()

    suspend fun patchPost(map: Map<String, String>, id: Int) =
        client.patch{
            url{ path("/posts/${id}") }
            contentType(ContentType.Application.Json)
            setBody(map)
        }.body<Post>()

    // in difference with patch, put changes all the fields of one entry
    suspend fun putPost(post:Post, id: Int) =
        client.patch{
            url{ path("/posts/${id}") }
            contentType(ContentType.Application.Json)
            setBody(post)
        }.body<Post>()

    suspend fun deletePost(id: Int): HttpResponse =
        client.delete("posts/${id}")

    // https://jsonplaceholder.typicode.com/comments?postId=1
    suspend fun commentsByIdQueryParameter(id: Int): List<Comment> =
        client.get{
            url{
                path("/comments")
                parameter("postId", id)
            }
        }.body<List<Comment>>()

    companion object {
        fun httpClient() = HttpClient {

            install(ContentNegotiation){
                json(json = Json{
                    ignoreUnknownKeys = true
                })
            }

            install(HttpTimeout){
                requestTimeoutMillis = 10_000L
                connectTimeoutMillis = 10_000L
                socketTimeoutMillis = 10_000L
            }

            install(DefaultRequest){
                url {
                    protocol = URLProtocol.HTTPS
                    host = "jsonplaceholder.typicode.com"
                    headers{
                        append("Content-Type", "application/json")
                        append(HttpHeaders.Authorization, "Bearer 1234567890")
                    }
                }
            }

            install(Logging){
                logger = Logger.ANDROID
                level = LogLevel.ALL // headers body info none
            }
        }
    }

}