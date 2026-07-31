package com.example.deeplinksparsing

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri

@Composable
fun MainScreen(
    data: Uri?
){

    var key by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val request = DeepLinkRequest(data ?: "nothing://none".toUri())
        key = deepLinkPatterns.firstOrNull {
            DeepLinkMatcher(request,it).match() != null
        }.let{ "${it?.pathSegments} ${request.pathSegments}"  }
    }

    Column(
        Modifier.statusBarsPadding().fillMaxSize()
    ){
        Text("hello")
        Text("deep link: $data")
        Text("key: $key")
    }

}

