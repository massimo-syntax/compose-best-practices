package com.example.testmvicleanarchitecture.feature_addedit.presentation

data class AddNoteUiState(
    val id: Long = 0L,
    val title: String = "",
    val content: String = "",
    val colorHex: String = "#F5F5F0",
    val isPinned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val errorMessage: String? = null
)
