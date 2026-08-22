package com.example.testmvicleanarchitecture.feature_addedit.domain

import com.example.testmvicleanarchitecture.core.data.local.FakeDao
import com.example.testmvicleanarchitecture.core.data.repository.NoteRepositoryImpl
import com.example.testmvicleanarchitecture.core.domain.model.Note
import com.example.testmvicleanarchitecture.core.domain.model.toNoteEntity
import com.example.testmvicleanarchitecture.core.domain.repository.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddEditNoteUseCaseTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeDao: FakeDao
    private lateinit var repository: NoteRepository
    private lateinit var useCase: AddEditNoteUseCase

    private lateinit var notes:List<Note>

    @Before
    fun setUp() {
        notes = mutableListOf(
            Note(
                id = 1,
                title = "Note 1",
                content = "Content 1",
                createdAt = 0L,
                colorHex = "",
                isPinned = false
            ),
            Note(
                id = 2,
                title = "Note 2",
                content = "Content 2",
                createdAt = 0L,
                colorHex = "",
                isPinned = false
            ),
            Note(
                id = 3,
                title = "Note 3",
                content = "Content 3",
                createdAt = 0L,
                colorHex = "",
                isPinned = false
            ),
            Note(
                id = 4,
                title = "Note 4",
                content = "Content 4",
                createdAt = 0L,
                colorHex = "",
                isPinned = false
            ),
            Note(
                id = 5,
                title = "Note 5",
                content = "Content 5",
                createdAt = 0L,
                colorHex = "",
                isPinned = false
            )
        )

        fakeDao = FakeDao(notes.map { it.toNoteEntity() }.toMutableList())
        repository = NoteRepositoryImpl(fakeDao, testDispatcher)
        useCase = AddEditNoteUseCase(repository)
    }


    @Test
    fun `Get 1 note, the note should be the same as teh one in the list`() = runTest {
        // Given
        val note = notes.first()

        // When
        val queriedNote = useCase.getNoteById(note.id)

        // Then
        assert(queriedNote == note)

    }

    @Test
        fun `When saving a note list size should be 6`() = runTest{
        // Given
        val newNote = Note(
            id = 0L,
            title = "Note 6",
            content = "Content 6",
            createdAt = 0L,
            colorHex = "",
            isPinned = false
        )

        // When
        useCase.saveNote(
            id = 0L,
            title = "Note 6",
            content = "Content 6",
            createdAt = 0L,
            colorHex = "",
            isPinned = false
        )
        val allNotes = repository.getAllNotes()
        // Then
        assert(allNotes.size == 6)
    }

}