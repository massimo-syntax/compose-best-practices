package com.example.testmvicleanarchitecture.core.data.local

import com.example.testmvicleanarchitecture.core.data.local.dao.NoteDao
import com.example.testmvicleanarchitecture.core.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeDao(
    private val notes: MutableList<NoteEntity> = mutableListOf()
) : NoteDao {

    private val _notesFlow = MutableStateFlow(notes.toList())

    override suspend fun getAllNotes(): List<NoteEntity> {
        return notes
    }

    override fun getAllNotesFlow(): Flow<List<NoteEntity>> {
        return _notesFlow.asStateFlow().map { list ->
            list.sortedWith(compareByDescending<NoteEntity> { it.isPinned }.thenByDescending { it.createdAt })
        }
    }

    override suspend fun searchNotes(query: String): List<NoteEntity> {
        return emptyList()
    }

    override suspend fun getNoteById(id: Long): NoteEntity? {
        return notes.find { it.id == id }
    }

    override suspend fun insertNote(note: NoteEntity): Long {
        val newNote = if (note.id == 0L) {
            val nextId = (notes.maxOfOrNull { it.id } ?: 0L) + 1L
            note.copy(id = nextId)
        } else {
            note
        }
        notes.add(newNote)
        refreshFlow()
        return newNote.id
    }

    override suspend fun updateNote(note: NoteEntity) {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
            refreshFlow()
        }
    }

    override suspend fun deleteNote(note: NoteEntity) {
        notes.remove(note)
        refreshFlow()
    }

    override suspend fun updatePinStatus(id: Long, isPinned: Boolean) {
        val index = notes.indexOfFirst { it.id == id }
        if (index != -1) {
            notes[index] = notes[index].copy(isPinned = isPinned)
            refreshFlow()
        }
    }

    private fun refreshFlow() {
        _notesFlow.value = notes.toList()
    }
}
