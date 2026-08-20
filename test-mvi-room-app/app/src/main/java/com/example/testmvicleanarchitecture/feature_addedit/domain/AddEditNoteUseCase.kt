package com.example.testmvicleanarchitecture.feature_addedit.domain

import com.example.testmvicleanarchitecture.core.domain.model.Note
import com.example.testmvicleanarchitecture.core.domain.repository.NoteRepository
import javax.inject.Inject

class AddEditNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend fun getNoteById(id: Long): Note? {
        return repository.getNoteById(id)
    }

    suspend fun saveNote(
        id: Long,
        title: String,
        content: String,
        colorHex: String,
        isPinned: Boolean,
        createdAt: Long
    ): Note {
        val note = Note(
            id = id,
            title = title.ifBlank { "Untitled Note" },
            content = content,
            createdAt = if (id > 0L) createdAt else System.currentTimeMillis(),
            colorHex = colorHex,
            isPinned = isPinned
        )
        return repository.saveNote(note)
    }
}
