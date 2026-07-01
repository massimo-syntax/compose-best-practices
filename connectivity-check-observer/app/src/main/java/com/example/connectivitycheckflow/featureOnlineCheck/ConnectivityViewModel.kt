package com.example.connectivitycheckflow.featureOnlineCheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connectivitycheckflow.connectivityObserver.ConnectivityObserver
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ConnectivityViewModel(
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    val isConnected =
        connectivityObserver
            .isConnected
            // convert the Flow into a StateFlow, making it lifecycle-aware and cacheable.
            .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        connectivityObserver.isCurrentlyConnected()
    )
}