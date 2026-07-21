package com.example.simplestprompttoai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.simplestprompttoai.ui.theme.SimplestPromptToAiTheme
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.HarmBlockThreshold
import com.google.firebase.ai.type.HarmCategory
import com.google.firebase.ai.type.SafetySetting
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val generativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
            modelName = "gemini-3.5-flash",
            generationConfig = generationConfig {
                temperature = 0f
            },
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, HarmBlockThreshold.LOW_AND_ABOVE),
                SafetySetting(HarmCategory.HATE_SPEECH, HarmBlockThreshold.LOW_AND_ABOVE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, HarmBlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, HarmBlockThreshold.ONLY_HIGH),
            )
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val aiResponse = mutableStateOf("Loading...")

        lifecycleScope.launch {
            val prompt = "can you please print me 3 lines of lorem ipsum?"
            try {
                aiResponse.value = generativeModel.generateContent(prompt).text ?: "ERROR: null"
            } catch (e: Exception) {
                aiResponse.value = "ERROR: ${e.message}"
            }
        }

        enableEdgeToEdge()
        setContent {
            SimplestPromptToAiTheme {
                Column{
                    Spacer(Modifier.height(16.dp))
                    Text("Random Lorem Ipsum prompt to ai")
                    Text(
                        "Ai response: ${aiResponse.value}",
                        color = if(aiResponse.value.contains("ERROR:")) Color.Red else Color.Black
                    )
                }
            }
        }
    }
}

