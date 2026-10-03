package com.example.ktorclient.client

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
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {

    private val client = httpClient()

    suspend fun getPosts(): List<Post> =
        client
            .get("posts")
            .body<List<Post>>()


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