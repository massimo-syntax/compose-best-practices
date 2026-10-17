package com.example.testdataflowviewmodelnetwork.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp


@Composable
fun LoadingContent(
    modifier: Modifier = Modifier
){
    Column(
        modifier.semantics{
            contentDescription = "Loading"
        }
    ) {
        CircularProgressIndicator(Modifier.size(100.dp))
    }

}