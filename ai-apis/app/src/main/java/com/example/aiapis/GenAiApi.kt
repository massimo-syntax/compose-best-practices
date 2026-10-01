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
import com.google.genai.kotlin.Client
import kotlinx.coroutines.launch




// fails with:
// java.lang.NoClassDefFoundError: Failed resolution of: Lio/ktor/client/plugins/HttpTimeout;
// at com.google.genai.kotlin.ApiClient.client$lambda$0(ApiClient.kt:126)


// @val client = Client(apiKey = "Sk.Ab8kdi702mdöTD6xydyFfawigvTeqIhf3-kH2snr-jN:tddtty")

@Composable
fun GenAiApi() {
    // api key from ai studio
    var models by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("") }

    val context = LocalContext.current
    val toast = { msg: Any? -> Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show() }

    val scope = rememberCoroutineScope()


    Column(
        Modifier.statusBarsPadding()
    ) {
        Text("open ai remote api")
        Button({
            scope.launch {
                try {
                    val client = Client(apiKey = "")
                    client.use { client ->
                        val apiResponse = client.models.generateContent(
                            model = "gemini-flash-latest",
                            text = "Just testing the api , sinding :ping , you can respond with just 'pong!'"
                        )
                        response = apiResponse.text ?: "response is null"
                    }

                    toast("prompted")
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