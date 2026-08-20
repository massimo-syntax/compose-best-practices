package com.example.testmvicleanarchitecture.core.domain.repository

import com.example.testmvicleanarchitecture.core.domain.model.Note

interface NoteRepository {
    suspend fun getAllNotes(): List<Note>
    suspend fun searchNotes(query: String): List<Note>
    suspend fun getNoteById(id: Long): Note?
    suspend fun saveNote(note: Note): Note
    suspend fun deleteNote(note: Note)
    suspend fun togglePin(id: Long, isPinned: Boolean)
}
