package com.example.testmvicleanarchitecture.data.repository

import com.example.testmvicleanarchitecture.data.local.dao.NoteDao
import com.example.testmvicleanarchitecture.data.local.entity.NoteEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val ioDispatcher: CoroutineDispatcher
) : NoteRepository {

    override fun getAllNotes(): Flow<List<NoteEntity>> {
        return noteDao.getAllNotes().flowOn(ioDispatcher)
    }

    override fun searchNotes(query: String): Flow<List<NoteEntity>> {
        return noteDao.searchNotes(query).flowOn(ioDispatcher)
    }

    override suspend fun getNoteById(id: Long): NoteEntity? {
        return withContext(ioDispatcher) {
            noteDao.getNoteById(id)
        }
    }

    override suspend fun saveNote(note: NoteEntity): Long {
        return withContext(ioDispatcher) {
            if (note.id == 0L) {
                noteDao.insertNote(note)
            } else {
                noteDao.updateNote(note)
                note.id
            }
        }
    }

    override suspend fun deleteNote(note: NoteEntity) {
        withContext(ioDispatcher) {
            noteDao.deleteNote(note)
        }
    }

    override suspend fun togglePin(id: Long, isPinned: Boolean) {
        withContext(ioDispatcher) {
            noteDao.updatePinStatus(id, isPinned)
        }
    }
}
