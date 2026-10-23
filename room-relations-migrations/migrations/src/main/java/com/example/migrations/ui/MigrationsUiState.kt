package com.example.migrations.ui

import com.example.migrations.data.DataEntity

data class MigrationsUiState(
    val textInput: String = "",
    val dataList: List<DataEntity> = emptyList(),
)
