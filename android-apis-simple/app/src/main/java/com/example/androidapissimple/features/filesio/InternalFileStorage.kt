package com.example.androidapissimple.features.filesio

import android.content.Context.MODE_PRIVATE
import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch


@Composable
fun InternalFileStorage(
    title: String
){
    var text by remember { mutableStateOf("") }
    var fileContent by remember { mutableStateOf("") }
    var filesList by remember { mutableStateOf(listOf<String>()) }

    val filename = "filename.txt"
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title)
        Text("Read / write own internal storage of the app, can be found only from the app, using context.MODE_PRIVATE")
        OutlinedTextField(
            value = text,
            onValueChange = { text = it }
        )
        Button(onClick = {
            if(text.isEmpty()) return@Button
            scope.launch {
                val fileOutputStream = context.openFileOutput(filename, MODE_PRIVATE)
                fileOutputStream.use { outputStream ->
                    outputStream.write(text.toByteArray())
                }
                Toast.makeText(context, "file saved", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Save text")
        }
        Button({
            try{
                val fileInputStream = context.openFileInput(filename)
                fileInputStream.bufferedReader().use{
                    fileContent = it.readText()
                }
                Toast.makeText(context, "reading from file", Toast.LENGTH_SHORT).show()
            }catch (e: Exception){
                Toast.makeText(context, "exception reading file: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Read file")
        }
        Text("File content: $fileContent")
        Button({
            filesList = context.fileList().toList()
        }){
            Text("Display files list")
        }
        filesList.forEach {
            Text(text = it)
        }
    }
}
