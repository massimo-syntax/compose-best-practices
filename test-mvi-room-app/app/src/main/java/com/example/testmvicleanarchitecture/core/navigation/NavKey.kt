package com.example.testmvicleanarchitecture.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
data object HomeScreenKey : NavKey


@Serializable
data class AddNoteKey(
    val noteId: Long = 0L
) : NavKey
