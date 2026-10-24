package com.example.migrations.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.migrations.data.DataDao
import com.example.migrations.data.DataEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MigrationsViewModel @Inject constructor(
    private val dataDao: DataDao
) : ViewModel() {

    private val _textInput = MutableStateFlow("")

    val uiState: StateFlow<MigrationsUiState> = combine(
        _textInput,
        dataDao.getAllData()
    ) { text, dataList ->
        MigrationsUiState(
            textInput = text,
            dataList = dataList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MigrationsUiState()
    )

    fun onTextInputChange(newText: String) {
        _textInput.value = newText
    }

    fun insertData() {
        val currentText = _textInput.value
        if (currentText.isNotBlank()) {
            viewModelScope.launch {
                dataDao.insertData(
                    DataEntity(
                        label = currentText.trim(),
                        // column added in the new migration
                        // createdAt = System.currentTimeMillis()
                    )
                )
                _textInput.value = ""
            }
        }
    }

    fun deleteData(dataEntity: DataEntity) {
        viewModelScope.launch {
            dataDao.deleteData(dataEntity)
        }
    }
}
