package com.example.workmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.WorkInfo
import com.example.workmanager.presentation.MyViewModel


@Composable
fun MyScreen(
    viewModel: MyViewModel = hiltViewModel()
) {
    val workState by viewModel.workState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchData("category_data")
    }

    when (workState?.state) {
        WorkInfo.State.SUCCEEDED -> {}
        WorkInfo.State.ENQUEUED ->  {}
        WorkInfo.State.CANCELLED -> {}
        WorkInfo.State.FAILED ->    {}
        WorkInfo.State.RUNNING ->   {}
        WorkInfo.State.BLOCKED ->   {}
        else -> Unit
    }


    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){

        Text("${workState?.state}")
        Text("${workState?.outputData}")
        Text("${workState?.outputData?.getString("DATA")}")


    }

}



