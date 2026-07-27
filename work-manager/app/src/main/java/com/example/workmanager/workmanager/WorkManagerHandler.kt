package com.example.workmanager.workmanager

import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface WorkManagerHandler {

    fun initializeYourWorker(
        inputData: Data,
        workQuery: (WorkQuery) -> Unit
    )
    fun observeWorkInfo(workQuery: WorkQuery): Flow<List<WorkInfo>>

}

class WorkManagerHandlerImpl @Inject constructor(
    private val workManager: WorkManager
) : WorkManagerHandler {

    override fun initializeYourWorker(
        inputData: Data,
        workQuery: (WorkQuery) -> Unit
    ) {
        val request = OneTimeWorkRequestBuilder<YourWorker>()
            .setInputData(inputData)
            .build()

        workManager.enqueue(request)

        val query = WorkQuery.fromIds(mutableListOf(request.id))
        workQuery(query)
    }

    override fun observeWorkInfo(workQuery: WorkQuery): Flow<List<WorkInfo>> {
        return workManager.getWorkInfosFlow(workQuery)
    }
}

