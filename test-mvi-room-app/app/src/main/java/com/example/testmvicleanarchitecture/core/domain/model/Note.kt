package com.example.testmvicleanarchitecture.core.domain.model

data class Note(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val createdAt: Long,
    val colorHex: String,
    val isPinned: Boolean
)
