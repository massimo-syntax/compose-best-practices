package com.example.workmanager.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.workmanager.data.RemoteDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope


@HiltWorker
class YourWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val remoteDataSource: RemoteDataSource  // injected by Hilt
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            val data = remoteDataSource.networkRequest()
            val dataOut = workDataOf("DATA" to data, "DATA2" to "nothing" )
            Result.success(dataOut)
        }catch (e: Exception){
            Result.failure()
        }
    }
}
