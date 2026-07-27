package com.example.workmanager.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkQuery
import androidx.work.workDataOf
import com.example.workmanager.workmanager.WorkManagerHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class MyViewModel @Inject constructor(
    private val workManagerHandler: WorkManagerHandler
) : ViewModel() {

    private val _workState = MutableStateFlow<WorkInfo?>(null)
    val workState: StateFlow<WorkInfo?> = _workState.asStateFlow()

    fun fetchData(dataType: String) {
        val inputData = workDataOf(
            "DATA_KEY" to dataType
        )
        workManagerHandler.initializeYourWorker(inputData) { workQuery ->
            observeWork(workQuery)
        }
    }
    private fun observeWork(workQuery: WorkQuery) {
        viewModelScope.launch {
            workManagerHandler
                .observeWorkInfo(workQuery)
                .collect { workInfoList ->
                    _workState.value = workInfoList.firstOrNull()
                }
        }
    }
}
