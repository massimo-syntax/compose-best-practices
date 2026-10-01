package com.example.aiapis

import android.content.Context
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
import com.google.mlkit.genai.common.DownloadStatus
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.prompt.Generation
import com.google.mlkit.genai.prompt.GenerativeModel
import kotlinx.coroutines.launch


// LOCAL AI NOT SUPPORTED IN THIS DEVICE
// ALSO IN THE EMULATOR WITH API 35. so i would skip for now
@Composable
fun MlKitGenAiOnDevice(){

    var message by remember{ mutableStateOf("") }
    var response by remember{ mutableStateOf("") }
    val context = LocalContext.current
    // Get a GenerativeModel instance
    val generativeModel = Generation.getClient()
    val scope = rememberCoroutineScope()

    Column(
        Modifier.statusBarsPadding()
    ){
        Text("here is some ai now..")
        Button({
            scope.launch {
                try {
                    checkStatus( generativeModel, context){ message = it }
                    generateResponse(generativeModel){ response = it }
                }catch (e:Exception){
                    message = e.localizedMessage
                }
            }
        }){
            Text("download/generate response")
        }
        Text("$message")
        Text("response: $response")
    }

}

suspend fun checkStatus(model: GenerativeModel, context: Context, updateMessage:(String)->Unit){
    val toast = { msg: Any? -> Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show() }

    val status = model.checkStatus()
    when (status) {
        FeatureStatus.UNAVAILABLE -> {
            val msg ="Gemini Nano not supported on this device or device hasn't fetched the latest configuration to support it"
            toast(msg)
        }
        FeatureStatus.DOWNLOADABLE -> {
            // Gemini Nano can be downloaded on this device, but is not currently downloaded
            model.download().collect { status ->
                when (status) {
                    is DownloadStatus.DownloadStarted -> {
                        val msg = "starting download for Gemini Nano"
                        toast(msg)
                    }
                    is DownloadStatus.DownloadProgress -> {
                        // val msg = ""
                        // toast(msg)
                        updateMessage("Nano ${status.totalBytesDownloaded} bytes downloaded")
                    }
                    DownloadStatus.DownloadCompleted -> {

                        val msg = "Download compoleted"
                        toast(msg)
                    }

                    is DownloadStatus.DownloadFailed -> {
                        val msg = "download failed"
                        toast(msg)
                    }
                }
            }
        }
        FeatureStatus.DOWNLOADING -> {
            //
            toast("Gemini Nano currently being downloaded")
        }
        FeatureStatus.AVAILABLE -> {
            //
            toast("Gemini Nano currently downloaded and available to use on this device")
        }
    }
}

suspend fun generateResponse(model: GenerativeModel, updateResponse: (String)->Unit){
    val response = model.generateContent("Write a 3 sentence story about a magical dog.")
//    val response = generativeModel.generateContent(
//        generateContentRequest(
//            TextPart("Write a 3 sentence story about a magical dog."),
//        ) {
//            // Optional parameters
//            temperature = 0.2f
//            topK = 10
//            candidateCount = 3
//        },
//    )
    updateResponse(response.candidates[0].text)
}
