package com.example.deeplinksparsing

import kotlinx.serialization.KSerializer

class DeepLinkMatcher(
    private val request: DeepLinkRequest,
    private val pattern: DeepLinkPattern<*>
) {
    fun match(): DeepLinkMatchResult? {
        // Verify scheme and host match
        if (request.scheme != pattern.scheme) return null
        if (request.host != pattern.host) return null

        // Compare path segments, extracting argument values
        // where pattern has placeholders like {filter}
        // val args = mutableMapOf<String, String>()

        // in this caw we just have path segments
        // home, user/123, no profile yet

        // let say very quickly, user.. /user/{id]
        val size = request.pathSegments.size
        val key: String = request.pathSegments.first()
        val id: String = request.pathSegments.last()

        if(pattern.pathSegments.first() == request.pathSegments.first()){
            val args = mapOf(key to id)
            return DeepLinkMatchResult(
                args = args,
                serializer = pattern.serializer
            )
        }
        return null
    }

}

data class DeepLinkMatchResult(
    val args: Map<String, String>,
    val serializer: KSerializer<*>
)