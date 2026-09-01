package com.example.androidapissimple

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.androidapissimple.features.broadcastreceiver.CUSTOM_INTENT_ACTION
import com.example.androidapissimple.features.broadcastreceiver.CustomBroadcastReceiver
import com.example.androidapissimple.ui.theme.AndroidApisSimpleTheme

class MainActivity : ComponentActivity() {
    private val receiver = CustomBroadcastReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidApisSimpleTheme {
                MainNavigationScreen()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(CUSTOM_INTENT_ACTION)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, intentFilter,RECEIVER_EXPORTED)
        }else{
            registerReceiver(receiver, intentFilter)
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }
}
