package com.example.testmvicleanarchitecture.core.data.local

import com.example.testmvicleanarchitecture.core.data.local.dao.NoteDao
import com.example.testmvicleanarchitecture.core.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeDao(
    private val notes: MutableList<NoteEntity> = mutableListOf()
) : NoteDao {

    override suspend fun getAllNotes(): List<NoteEntity> {
        return notes
    }

    fun getNotesFlow() = flowOf(notes.toList())

    override suspend fun searchNotes(query: String): List<NoteEntity> {
        return emptyList()
    }

    override suspend fun getNoteById(id: Long): NoteEntity? {
        return notes.find { it.id == id }
    }

    override suspend fun insertNote(note: NoteEntity): Long {
        notes.add(note)
        return note.id
    }

    override suspend fun updateNote(note: NoteEntity) {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
        }
    }

    override suspend fun deleteNote(note: NoteEntity) {
        notes.remove(note)
    }

    override suspend fun updatePinStatus(id: Long, isPinned: Boolean) {
        val index = notes.indexOfFirst { it.id == id }
        if (index != -1) {
            notes[index] = notes[index].copy(isPinned = isPinned)
        }
    }
}
