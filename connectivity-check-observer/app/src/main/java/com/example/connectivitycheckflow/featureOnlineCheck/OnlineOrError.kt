package com.example.connectivitycheckflow.featureOnlineCheck

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun OnlineOrErrorScreen(viewModel: ConnectivityViewModel) {

    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

    if (isConnected)
        ApplicationOnline()
    else
        ApplicationOffline()

}