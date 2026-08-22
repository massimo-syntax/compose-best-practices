package com.example.testmvicleanarchitecture.core.data.repository

import app.cash.turbine.test
import com.example.testmvicleanarchitecture.core.data.local.FakeDao
import com.example.testmvicleanarchitecture.core.domain.model.Note
import com.example.testmvicleanarchitecture.core.domain.model.toNoteEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotesRepositoryImplTest {

    private lateinit var repository: NoteRepositoryImpl
    private lateinit var fakeDao: FakeDao
    private lateinit var notes: MutableList<Note>
    private val testDispatcher = UnconfinedTestDispatcher()

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
    }

    @Test
    fun `Get all notes returns notes from repository`() = runTest {
        val allNotes = repository.getAllNotes()
        assertThat(allNotes.size).isEqualTo(5)
    }

    @Test
    fun `When choosing a note, get same note queried from id`() = runTest {
        // Given
        val allNotes = repository.getAllNotes()
        // When
        val note = allNotes.last()
        val foundNote = repository.getNoteById(note.id)
        // Then
        assertThat(foundNote).isEqualTo(note)
    }

    @Test
    fun `When get note from wrong id, expect null`() = runTest {
        // Given
        val id = 87436374891
        // When
        val note = repository.getNoteById(id)
        // Then
        assertThat(note).isEqualTo(null)
    }


    @Test
    fun `Add 1 note expect list size to be 6`() = runTest {
        // Given
        val note = Note(
            id = 0L,
            title = "Note 6",
            content = "Content 6",
            createdAt = System.currentTimeMillis(),
            isPinned = false,
            colorHex = "#FFFFFF"
        )

        // When
        repository.saveNote(note)
        val allNotes = repository.getAllNotes()

        // Then
        assertThat(allNotes.size).isEqualTo(6)
        assertThat(allNotes.any { it.title == "Note 6" }).isTrue()
    }

    @Test
    fun `when inserting 1 note, the return value of the function is the same note`() = runTest {
        // Given
        val note = Note(0L, "Note 6", "Content 6", System.currentTimeMillis(), "#FFFFFF", false)
        val otherNote = notes.first()
        // When
        val sameNote = repository.saveNote(note)
        // Then
        assertThat(sameNote.copy(id = 0L)).isEqualTo(note)
        assertThat(note).isNotEqualTo(otherNote)
    }

    @Test
    fun `Update 1 note expect note updated and same list size`() = runTest {
        // Given
        val note = notes.first().copy(
            title = "new title",
            content = "new content",
            createdAt = System.currentTimeMillis()
        )
        val listSize = repository.getAllNotes().size

        // When
        val updatedNote = repository.saveNote(note)
        val sameListSize = repository.getAllNotes().size

        // Then
        assertThat(note).isEqualTo(updatedNote)
        assertThat(listSize).isEqualTo(sameListSize)
    }

    @Test
    fun `Delete existing note expect list size to be 4, and id is not found`() = runTest {
        // Given
        val note = notes.first()
        val size = repository.getAllNotes().size
        // When
        repository.deleteNote(note)
        val allNotes = repository.getAllNotes()
        // Then
        assertThat(allNotes.size).isEqualTo(size - 1)
        assertThat(allNotes.any { it.id == note.id }).isFalse()
    }

    @Test
    fun `When saveNote is called, getAllNotesFlow emits updated list`() = runTest {
        // Given
        val newNote = Note(
            id = 0L,
            title = "Flow Note",
            content = "Reactive Content",
            createdAt = 0L,
            colorHex = "",
            isPinned = false
        )

        repository.getAllNotesFlow().test {
            // 1. Initial emission (the 5 notes from setUp)
            val initialList = awaitItem()
            assertThat(initialList.size).isEqualTo(5)

            // 2. When we save a note
            repository.saveNote(newNote)

            // 3. Flow should automatically emit again with the new note
            val updatedList = awaitItem()
            assertThat(updatedList.size).isEqualTo(6)
            assertThat(updatedList.any { it.title == "Flow Note" }).isTrue()

            // Ensure no more unexpected emissions
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `When updating a note, getAllNotesFlow emits updated list`() = runTest {
        // Given
        val updatedNote = notes.first().copy(
            title = "Updated Title",
            content = "Updated Content",
            createdAt = System.currentTimeMillis()
        )

        val items = mutableListOf<Note>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.getAllNotesFlow().collect { list ->
                items.removeAll(items)
                list.forEach { items.add(it) }
            }
        }

        // When updating item
        repository.saveNote(updatedNote)
        testScheduler.advanceUntilIdle()

        // Then teh first note is updated
        assertThat(items.size).isEqualTo(5)
        assertThat(updatedNote).isEqualTo(items.first())

    }



}



