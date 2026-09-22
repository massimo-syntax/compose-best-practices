package com.example.androidapissimple.features.filesio

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun StorageAccessFrameworkScreen(
    title: String
){
    var text by remember { mutableStateOf("") }
    var fileText by remember { mutableStateOf("") }

    val filename = "filename.txt"
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    val fileLauncherWrite = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri ->
        uri?.let{
            contentResolver.openOutputStream(it)?.use{ stream ->
                stream.write(text.toByteArray())
            }
            Toast.makeText(context, "File saved",Toast.LENGTH_SHORT).show()
        }
    }

    val fileLauncherRead = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ){ uri ->
        uri?.let{
            val content = contentResolver.openInputStream(it)?.bufferedReader()?.use{
                it.readText()
            }
             fileText = content ?: "Failed opening file"
        }
    }




    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title)
        Text("Read / write to external storage, with the Storage Access Framework api")
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = {Text("File content")}
        )
        Button(onClick = {
            if(text.isEmpty()) return@Button
            fileLauncherWrite.launch(filename)
        }) {
            Text("Save File into a folder")
        }
        Button({
            fileLauncherRead.launch(arrayOf("text/plain"))
        }) {
            Text("open a text file")
        }
        Text("File content: $fileText")

    }
}