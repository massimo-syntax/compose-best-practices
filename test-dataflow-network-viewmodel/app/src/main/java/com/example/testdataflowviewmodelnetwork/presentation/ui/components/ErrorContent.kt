package com.example.testdataflowviewmodelnetwork.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@Composable
fun ShowError(
    modifier: Modifier = Modifier,
    message: String
){
    Column(
        modifier.semantics{
            contentDescription = "Error"
        }
    ) {
        Text("error")
        Text(message)
    }

}