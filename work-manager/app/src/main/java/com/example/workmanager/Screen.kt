package com.example.workmanager

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

@Composable
fun Screen(){

    val context = LocalContext.current

    var value by remember { mutableStateOf("something")}
    var finished by remember { mutableStateOf("not started")}

    LaunchedEffect(false) {
        val workInfoFlow = startWorkManager(context)
        workInfoFlow.collect{ workInfo ->
            if(workInfo == null){
                value = "work info is null: workInfo == $workInfo"
                return@collect
            }
            when(workInfo.state){
                WorkInfo.State.SUCCEEDED -> {}
                WorkInfo.State.ENQUEUED ->  {}
                WorkInfo.State.CANCELLED -> {}
                WorkInfo.State.FAILED ->    {}
                WorkInfo.State.RUNNING ->   {}
                WorkInfo.State.BLOCKED ->   {}
            }

            if(workInfo.state.isFinished){
                finished = "finished"
            }else{
                finished = "work initialized..."
            }
            value = workInfo.state.name
        }
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text("$value")
        Text("$finished")
    }

}

private fun startWorkManager(context: Context): Flow<WorkInfo?> {
    val work = OneTimeWorkRequestBuilder<MyWorker>()
        .setInputData(
            workDataOf(
                "title" to "My First Post",
                "content" to "Hello WorkManager"
            )
        )
        .addTag("tag")
        .setInitialDelay(1, TimeUnit.SECONDS)
        .build()

    val worker = WorkManager.getInstance(context)
    worker.enqueue(work)

    return worker.getWorkInfoByIdFlow(work.id)

}