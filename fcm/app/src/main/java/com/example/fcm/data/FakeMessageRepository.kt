package com.example.fcm.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FakeMessageRepository {

    private val initialMessages = listOf(
        "Welcome! Your message inbox is initialized.",
        "System update completed successfully."
    )

    private val _messages = MutableStateFlow<List<String>>(initialMessages)
    val messages: StateFlow<List<String>> = _messages.asStateFlow()

    fun reloadMessages(customInfo: String? = null) {
        val timeStamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val newMessage = if (customInfo != null) {
            "[$timeStamp] Remote update: $customInfo"
        } else {
            "[$timeStamp] Fake message #${_messages.value.size + 1} loaded automatically."
        }

        _messages.value = listOf(newMessage) + _messages.value
    }
}
