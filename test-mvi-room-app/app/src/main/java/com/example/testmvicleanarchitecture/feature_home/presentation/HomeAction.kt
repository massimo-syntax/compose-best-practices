package com.example.testmvicleanarchitecture.feature_home.presentation

import com.example.testmvicleanarchitecture.core.domain.model.Note

interface HomeAction {
    data class Delete(val note:Note) : HomeAction
    data class TogglePin(val note: Note) : HomeAction
    data class SearchQueryChange(val query: String) : HomeAction
}