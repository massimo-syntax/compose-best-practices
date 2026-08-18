package com.example.testmvicleanarchitecture.data.repository

import com.example.testmvicleanarchitecture.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<NoteEntity>>
    fun searchNotes(query: String): Flow<List<NoteEntity>>
    suspend fun getNoteById(id: Long): NoteEntity?
    suspend fun saveNote(note: NoteEntity): Long
    suspend fun deleteNote(note: NoteEntity)
    suspend fun togglePin(id: Long, isPinned: Boolean)
}
