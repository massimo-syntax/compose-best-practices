package com.example.workmanager.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class MyWorker(
    private val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("98347819fdsaq", "doing work")
        return try {
            Log.d("98347819fdsaq", "try")
            someInternalWork()
        } catch (e: Exception) {
            Log.d("98347819fdsaq", "do work exception ${e.message}")
            Result.failure()
        }
    }

    private suspend fun someInternalWork(): Result {
        return try {
            delay(3000)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}