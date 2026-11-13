package com.example.fcm.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fcm.data.FakeMessageRepository
import com.example.fcm.data.TokenDataStore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val tokenDataStore: TokenDataStore,
    private val messageRepository: FakeMessageRepository = FakeMessageRepository
) : ViewModel() {

    val fcmToken: StateFlow<String?> = tokenDataStore.tokenFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "Loading token from DataStore..."
    )

    val messages: StateFlow<List<String>> = messageRepository.messages

    init {
        // Fetch current FCM token and persist in DataStore if available
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { token ->
                    saveToken(token)
                }
            }
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            tokenDataStore.saveToken(token)
        }
    }

    fun manualReload() {
        messageRepository.reloadMessages("Manual user refresh")
    }

    companion object {
        fun provideFactory(
            tokenDataStore: TokenDataStore,
            repository: FakeMessageRepository = FakeMessageRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(tokenDataStore, repository) as T
            }
        }
    }
}
