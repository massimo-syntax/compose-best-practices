package com.example.aiapis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.aiapis.ui.theme.AiApisTheme
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDebug()
        enableEdgeToEdge()
        setContent {
            AiApisTheme {
                FirebaseAiLogic()
            }
        }
    }

    private fun initDebug() {
        // [START appcheck_initialize_debug]
        Firebase.initialize(this)
        val abc = Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )
        // [END appcheck_initialize_debug]
    }

    // generate sha256 instruction firebase ai app check @ 9.2026
    // keytool -list -v \
    // -alias androiddebugkey -keystore ~/.android/debug.keystore
    // password is android

    // Fehlerbehebungstoken
    //
    // tag:
    //  com.google.firebase.appcheck.debug.internal.DebugAppCheckProvider
    // text
    // Firebase App Check debug token: d4aac299-45a6-3th3r3um-4e9a-aad6-b994232a9032

}
