package com.example.testmvicleanarchitecture.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.testmvicleanarchitecture.ui.add_edit.AddNoteScreen
import com.example.testmvicleanarchitecture.ui.home.HomeScreen


@Composable
fun NotesNavHost() {

    val backStack = rememberNavBackStack(HomeScreenKey)

    // Intercept hardware/gesture system back when on AddNoteScreen
    BackHandler(enabled = backStack.size > 1) {
        backStack.removeLastOrNull()
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            // Home Screen
            entry<HomeScreenKey> {
                HomeScreen(
                    onNavigateToAddNote = {
                        backStack.add(AddNoteKey(noteId = 0L))
                    },
                    onNavigateToEditNote = { noteId ->
                        backStack.add(AddNoteKey(noteId = noteId))
                    }
                )
            }

            // Add / Edit Screen
            entry<AddNoteKey> { key ->
                AddNoteScreen(
                    noteId = key.noteId,
                    onNavigateBack = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}
