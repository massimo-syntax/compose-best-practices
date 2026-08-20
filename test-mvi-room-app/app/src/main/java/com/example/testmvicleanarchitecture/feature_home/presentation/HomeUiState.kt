package com.example.testmvicleanarchitecture.feature_home.presentation

import com.example.testmvicleanarchitecture.core.domain.model.Note

data class HomeUiState(
    val notes: List<Note> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)
