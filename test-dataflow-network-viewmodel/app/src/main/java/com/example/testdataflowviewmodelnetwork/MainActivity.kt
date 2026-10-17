package com.example.testdataflowviewmodelnetwork

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.testdataflowviewmodelnetwork.ui.theme.TestDataFlowViewModelNetworkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestDataFlowViewModelNetworkTheme {

            }
        }
    }
}

