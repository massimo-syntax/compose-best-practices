package com.example.testmvicleanarchitecture.feature_home.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import com.example.testmvicleanarchitecture.core.domain.model.Note
import com.example.testmvicleanarchitecture.ui.theme.MyTheme
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_loadingState_showsCircularProgressIndicator() {
        composeTestRule.setContent {
            MyTheme {
                HomeScreenContent(
                    uiState = HomeUiState(isLoading = true),
                    onAction = {},
                    onNavigateToAddNote = {},
                    onNavigateToEditNote = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun homeScreen_emptyState_showsNoNotesText() {
        val emptyMessage = "No notes yet. Tap + to write your first note!"
        
        composeTestRule.setContent {
            MyTheme {
                HomeScreenContent(
                    uiState = HomeUiState(notes = emptyList(), isLoading = false),
                    onAction = {},
                    onNavigateToAddNote = {},
                    onNavigateToEditNote = {}
                )
            }
        }

        composeTestRule.onNodeWithText(emptyMessage).assertIsDisplayed()
    }

    @Test
    fun homeScreen_withNotes_showsNotes() {
        val notes = listOf(
            Note(
                id = 1,
                title = "Note 1",
                content = "Content 1",
                createdAt = System.currentTimeMillis(),
                colorHex = "#1E293B",
                isPinned = false
            ),
            Note(
                id = 2,
                title = "Note 2",
                content = "Content 2",
                createdAt = System.currentTimeMillis(),
                colorHex = "#1E293B",
                isPinned = true
            )
        )
        
        composeTestRule.setContent {
            MyTheme {
                HomeScreenContent(
                    uiState = HomeUiState(notes = notes, isLoading = false),
                    onAction = {},
                    onNavigateToAddNote = {},
                    onNavigateToEditNote = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Note 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Note 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Content 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Content 2").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsAddNoteFab() {
        composeTestRule.setContent {
            MyTheme {
                HomeScreenContent(
                    uiState = HomeUiState(),
                    onAction = {},
                    onNavigateToAddNote = {},
                    onNavigateToEditNote = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Add Note").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsSearchBar() {
        composeTestRule.setContent {
            MyTheme {
                HomeScreenContent(
                    uiState = HomeUiState(),
                    onAction = {},
                    onNavigateToAddNote = {},
                    onNavigateToEditNote = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Search your notes...").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
    }

    @Test
    fun navigateToAddNote_callsOnNavigateToAddNote(){
        var navigateToAddNoteCalled = false
        val onNavigateToAddNote: () -> Unit = { navigateToAddNoteCalled = true }
        composeTestRule.setContent {
            MyTheme {
                HomeScreenContent(
                    uiState = HomeUiState(),
                    onAction = {},
                    onNavigateToAddNote = onNavigateToAddNote,
                    onNavigateToEditNote = {}
                )
            }
        }
        composeTestRule.onNodeWithContentDescription("Add Note").performClick()
        assertThat(navigateToAddNoteCalled).isTrue()
    }

    @Test
    fun tapOnAnyItem_navigateToEditNote(){
        var noteToEdit = -1L
        val onNavigateToEditNote: (Long) -> Unit = { noteToEdit = it }
        val note = Note(
            id = 1,
            title = "Note 1",
            content = "Content 1",
            createdAt = System.currentTimeMillis(),
            colorHex = "#1E293B",
            isPinned = false
        )
        val note2 = note.copy(id = 2, title = "Note 2")
        val note3 = note.copy(id = 3, title = "Note 3")

        composeTestRule.setContent {
            MyTheme {
                HomeScreenContent(
                    uiState = HomeUiState(notes = listOf(note, note2, note3)),
                    onAction = {},
                    onNavigateToAddNote = {},
                    onNavigateToEditNote = onNavigateToEditNote
                )
            }
        }
        composeTestRule.onNodeWithText("Note 1").performClick()
        assertThat(noteToEdit).isEqualTo(1)
        composeTestRule.onNodeWithText("Note 2").performClick()
        assertThat(noteToEdit).isEqualTo(2)
        composeTestRule.onNodeWithText("Note 3").performClick()
        assertThat(noteToEdit).isEqualTo(3)
    }
}
