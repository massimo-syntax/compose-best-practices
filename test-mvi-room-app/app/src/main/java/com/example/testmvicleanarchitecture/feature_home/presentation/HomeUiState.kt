package com.example.testmvicleanarchitecture.ui.home

import com.example.testmvicleanarchitecture.data.local.entity.NoteEntity


data class HomeUiState(
    val notes: List<NoteEntity> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val userMessage: String? = null
)
