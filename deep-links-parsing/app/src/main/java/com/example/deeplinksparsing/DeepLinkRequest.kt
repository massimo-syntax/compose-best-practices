package com.example.deeplinksparsing

import android.net.Uri

class DeepLinkRequest(uri: Uri) {
    val scheme: String? = uri.scheme
    val host: String? = uri.host
    val pathSegments: List<String> = uri.pathSegments
    val queryParameters: Map<String, String?> = uri.queryParameterNames
        .associateWith { uri.getQueryParameter(it) }
}