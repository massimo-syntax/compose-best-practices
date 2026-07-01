package com.example.connectivitycheckflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.connectivitycheckflow.connectivityObserver.MyConnectivityObserver
import com.example.connectivitycheckflow.featureOnlineCheck.ConnectivityViewModel
import com.example.connectivitycheckflow.featureOnlineCheck.OnlineOrErrorScreen
import com.example.connectivitycheckflow.ui.theme.ConnectivityCheckFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.10.1'
            val connectivityViewModel = viewModel<ConnectivityViewModel> {
                ConnectivityViewModel(
                    connectivityObserver = MyConnectivityObserver(
                        context = applicationContext
                    )
                )
            }
            ConnectivityCheckFlowTheme {
                OnlineOrErrorScreen(connectivityViewModel)
            }
        }
    }
}





