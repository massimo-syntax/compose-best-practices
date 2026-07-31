package com.example.deeplinksparsing

import android.net.Uri
import androidx.core.net.toUri
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.serialization.NavKeySerializer
import kotlinx.serialization.KSerializer

class DeepLinkPattern<T : NavKey>(
    val serializer: KSerializer<T>,
    val uri: Uri
) {
    val scheme: String? = uri.scheme
    val host: String? = uri.host
    val pathSegments: List<String> = uri.pathSegments
    // no need to encapsulate query parameters
}

val deepLinkPatterns: List<DeepLinkPattern<out NavKey>> = listOf(
    // Exact match: "https://www.myapp.com/home"
    DeepLinkPattern(
        NavKeySerializer<HomeKey>(),
        "deeplinkapp://myapp/home".toUri()
    ),

    // Path arguments: "https://www.myapp.com/users/{filter}"
    DeepLinkPattern(
        NavKeySerializer<UserKey>(),
        "deeplinkapp://myapp/user/{id}".toUri()
    ),

    // Query arguments: "https://www.myapp.com/search?firstName=...&age=..."
    DeepLinkPattern(
        NavKeySerializer<ProfileKey>(),
        "deeplinkapp://myapp/profile?{name}&{picture}&{favoritefood}".toUri()
    )
)