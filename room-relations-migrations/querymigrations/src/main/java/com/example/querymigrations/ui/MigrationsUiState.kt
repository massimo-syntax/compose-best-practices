package com.example.querymigrations.ui

import com.example.querymigrations.data.DataEntity

data class MigrationsUiState(
    val textInput: String = "",
    val dataList: List<DataEntity> = emptyList(),
)
