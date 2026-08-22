package com.example.testmvicleanarchitecture.core.data.repository

import com.example.testmvicleanarchitecture.core.data.local.dao.NoteDao
import com.example.testmvicleanarchitecture.core.domain.model.Note
import com.example.testmvicleanarchitecture.core.domain.model.toNote
import com.example.testmvicleanarchitecture.core.domain.model.toNoteEntity
import com.example.testmvicleanarchitecture.core.domain.model.toNoteList
import com.example.testmvicleanarchitecture.core.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val ioDispatcher: CoroutineDispatcher
) : NoteRepository {

    override suspend fun getAllNotes(): List<Note> {
        return withContext(ioDispatcher) {
            noteDao.getAllNotes().toNoteList()
        }
    }

    override fun getAllNotesFlow(): Flow<List<Note>> {
        return noteDao.getAllNotesFlow()
            .map { it.toNoteList() }
            .flowOn(ioDispatcher)
    }

    override suspend fun searchNotes(query: String): List<Note> {
        return withContext(ioDispatcher) {
            noteDao.searchNotes(query).toNoteList()
        }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return withContext(ioDispatcher) {
            noteDao.getNoteById(id)?.toNote()
        }
    }

    override suspend fun saveNote(note: Note): Note {
        return withContext(ioDispatcher) {
            val entity = note.toNoteEntity()
            if (entity.id == 0L) {
                val newId = noteDao.insertNote(entity)
                entity.copy(id = newId).toNote()
            } else {
                noteDao.updateNote(entity)
                entity.toNote()
            }
        }
    }

    override suspend fun deleteNote(note: Note) {
        withContext(ioDispatcher) {
            noteDao.deleteNote(note.toNoteEntity())
        }
    }

    override suspend fun togglePin(id: Long, isPinned: Boolean) {
        withContext(ioDispatcher) {
            noteDao.updatePinStatus(id, isPinned)
        }
    }
}
