package com.example.androidapissimple.features.filesio

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


@Composable
fun ExternalFileStorageContentResolverScreen(
    title: String
){
    var text by remember { mutableStateOf("") }
    var fileContent by remember { mutableStateOf("") }

    val filename = "filename.txt"
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title)
        Text("Read / write to external storage, public folders e.g Downloads")
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = {Text("File content")}
        )
        Button(onClick = {
            if(text.isEmpty()) return@Button
            saveFile(context, filename, text)
        }) {
            Text("Save File")
        }
        Button({
            fileContent = readFile(context, filename)
        }) {
            Text("Read file")
        }
        Text("File content: $fileContent")

    }
}


private fun saveFile(context: Context, fileName: String, content:String){
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
        Toast.makeText(context, "Build.VERSION.SDK_INT < Build.VERSION_CODES.Q", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "${Build.VERSION.SDK_INT} < ${Build.VERSION_CODES.Q}", Toast.LENGTH_SHORT).show()
        return
    }

    val contentResolver = context.contentResolver

    val values = ContentValues().apply{
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/")
    }

    val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
    uri?.let{
        contentResolver.openOutputStream(it)?.use{ stream ->
            stream.write(content.toByteArray())
        }
        Toast.makeText(context, "file $fileName saved into Downloads/", Toast.LENGTH_SHORT).show()
    }
}

private fun readFile(context: Context, fileName: String ): String{
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
        Toast.makeText(context, "Build.VERSION.SDK_INT < Build.VERSION_CODES.Q", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "${Build.VERSION.SDK_INT} < ${Build.VERSION_CODES.Q}", Toast.LENGTH_SHORT).show()
        return "Build version not supported"
    }

    val contentResolver = context.contentResolver
    val uri = contentResolver.query(
        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
        arrayOf(MediaStore.MediaColumns._ID),
        MediaStore.MediaColumns.DISPLAY_NAME + " = ?",
        arrayOf(fileName),
        null
    )?.use{ cursor ->
        if(cursor.moveToFirst()){
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
            ContentUris.withAppendedId(MediaStore.Downloads.EXTERNAL_CONTENT_URI, id)
        }else null
    }
    return uri?.let { contentResolver.openInputStream(it)?.bufferedReader()?.readText() } ?: "error reading the file"
}
