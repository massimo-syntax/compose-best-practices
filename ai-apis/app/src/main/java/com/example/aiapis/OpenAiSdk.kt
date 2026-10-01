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
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.Model
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.api.response.ResponseInput
import com.aallam.openai.api.response.ResponseRequest
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
// https://github.com/Aallam/openai-kotlin

// Or you can create an instance of OpenAI using a pre-configured OpenAIConfig:
//    val config = OpenAIConfig(
//        token = "",
//        timeout = Timeout(socket = 60.seconds),
//        // additional configurations...
//    )
//    val openAI = OpenAI(config)
@Composable
fun OpenAiScreen(){
    // https://github.com/Aallam/openai-kotlin

    var models by remember { mutableStateOf("") }
    var message by remember{ mutableStateOf("") }
    var response by remember{ mutableStateOf("") }

    val context = LocalContext.current
    val toast = { msg: Any? -> Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show() }

    val scope = rememberCoroutineScope()

    val openai = OpenAI(
        token = "api key free plan: 'you have no credits remained'",
        timeout = Timeout(socket = 60.seconds),
        // additional configurations...
    )

    Column(
        Modifier.statusBarsPadding()
    ){
        Text("open ai remote api")
        Button({
            scope.launch {
                try {
                    //updateModelsList(openai){ message = it }
                    //message = "prompting.. "
                    //prompt(openai){ message = it }
                    toast("prompted")
                }catch (e:Exception){
                    message = e.localizedMessage ?: "exception"
                    toast(e.localizedMessage)
                }
            }
        }){
            Text("generate response")
        }
        Text("$message")
        Text("response: $response")
    }

}

suspend fun updateModelsList(openai: OpenAI, update: (String)->Unit){
    val models: List<Model> = openai.models()
    update(models.toString())
}

suspend fun prompt(openAi: OpenAI, update:(String)->Unit){
    //val model = "gpt-5-mini"
    //val id = ModelId("gpt-4.1")
    //val model: Model = openAi.model(id)
    val response = openAi.response(
        request = ResponseRequest(
            model = ModelId("gpt-4.1"),
            input = ResponseInput("Write a haiku about Kotlin.")
        )
    )
    update(response.outputText ?: "response is: null")
}