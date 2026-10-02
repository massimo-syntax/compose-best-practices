package com.example.aiapis

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.launch


@Composable
fun FirebaseAiLogic(){

    var message by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("") }

    val context = LocalContext.current
    val toast = { msg: Any? -> Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show() }

    val scope = rememberCoroutineScope()

    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-3.8-flash")


    Column(
        Modifier.statusBarsPadding()
    ) {
        Text("open ai remote api")
        Button({
            scope.launch {
                message = "generating response"
                try {
                    val prompt = "Just testing the api, i say ping, you can also just send: 'pong!' "
                    // To generate text output, call generateContent with the text input
                    val aiResponse = model.generateContent(prompt)
                    response = aiResponse.text ?: "response is null"
                    toast("prompted")
                    message = "response generated!"
                } catch (e: Exception) {
                    message = e.localizedMessage ?: "exception"
                    toast(e.localizedMessage)
                }
            }
        }) {
            Text("generate response")
        }
        Text("$message")
        Text("response: $response")
    }


}