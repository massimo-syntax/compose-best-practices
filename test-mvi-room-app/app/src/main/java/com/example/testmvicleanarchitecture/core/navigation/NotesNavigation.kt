package com.example.testmvicleanarchitecture.core.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.testmvicleanarchitecture.feature_addedit.presentation.AddNoteScreen
import com.example.testmvicleanarchitecture.feature_home.presentation.HomeScreen


@Composable
fun NotesNavHost() {

    val backStack = rememberNavBackStack(HomeScreenKey)
    val saveableStateHolder = rememberSaveableStateHolder()

    // Intercept hardware/gesture system back when on AddNoteScreen
    BackHandler(enabled = backStack.size > 1) {
        backStack.removeLastOrNull()
    }

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(saveableStateHolder),
            rememberViewModelStoreNavEntryDecorator()
        ),
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
