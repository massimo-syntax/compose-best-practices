package com.example.testmvicleanarchitecture.ui.add_edit

data class AddNoteUiState(
    val id: Long = 0L,
    val title: String = "",
    val content: String = "",
    val colorHex: String = "#F5F5F0",
    val isPinned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)
