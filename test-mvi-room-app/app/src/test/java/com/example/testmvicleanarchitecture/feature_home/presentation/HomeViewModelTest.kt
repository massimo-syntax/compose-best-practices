package com.example.testmvicleanarchitecture.feature_home.presentation

import app.cash.turbine.test
import com.example.testmvicleanarchitecture.core.data.local.FakeDao
import com.example.testmvicleanarchitecture.core.data.repository.NoteRepositoryImpl
import com.example.testmvicleanarchitecture.core.domain.model.Note
import com.example.testmvicleanarchitecture.core.domain.model.toNoteEntity
import com.example.testmvicleanarchitecture.core.domain.repository.NoteRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: NoteRepository
    private lateinit var fakeDao: FakeDao
    private lateinit var viewModel: HomeViewModel

    private val initialNotes = listOf(
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

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeDao(initialNotes.map { it.toNoteEntity() }.toMutableList())
        repository = NoteRepositoryImpl(fakeDao, testDispatcher)
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ai generated __ uiState initially emits notes from repository`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.notes).hasSize(5)
            assertThat(state.isLoading).isFalse()
        }
    }


    @Test
    fun `SearchQueryChange action updates uiState and filters notes`() = runTest {
        viewModel.uiState.test {
            awaitItem() // Initial emission

            viewModel.action(HomeAction.SearchQueryChange("Note 1"))

            val filteredState = awaitItem()
            assertThat(filteredState.searchQuery).isEqualTo("Note 1")
            assertThat(filteredState.notes).hasSize(1)
            assertThat(filteredState.notes[0].title).isEqualTo("Note 1")

            viewModel.action(HomeAction.SearchQueryChange("NonExistent"))
            val emptyState = awaitItem()
            assertThat(emptyState.notes).isEmpty()
            assertThat(emptyState.searchQuery).isEqualTo("NonExistent")
        }
    }

    @Test
    fun `Delete action removes note from repository and updates uiState`() = runTest {
        viewModel.uiState.test {
            awaitItem() // Initial emission

            viewModel.action(HomeAction.Delete(initialNotes[0]))

            val updatedState = awaitItem()
            assertThat(updatedState.notes).hasSize(4)
            assertThat(updatedState.notes[0].id).isEqualTo(2)
        }
    }

    @Test
    fun `TogglePin action updates note in repository and uiState`() = runTest {
        viewModel.uiState.test {
            awaitItem() // Initial emission

            viewModel.action(HomeAction.TogglePin(initialNotes[0]))

            val updatedState = awaitItem()
            // FakeDao.getAllNotesFlow() sorts by isPinned descending
            assertThat(updatedState.notes[0].id).isEqualTo(1)
            assertThat(updatedState.notes[0].isPinned).isTrue()
        }
    }
}