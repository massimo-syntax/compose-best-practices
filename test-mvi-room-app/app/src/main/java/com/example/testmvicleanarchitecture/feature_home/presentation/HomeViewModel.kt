package com.example.testmvicleanarchitecture.feature_home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testmvicleanarchitecture.core.domain.model.Note
import com.example.testmvicleanarchitecture.core.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(
        _notes,
        _searchQuery,
        _isLoading
    ) { notes, query, loading ->
        HomeUiState(
            notes = notes,
            searchQuery = query,
            isLoading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(isLoading = true)
    )

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            val query = _searchQuery.value
            val result = if (query.isBlank()) {
                repository.getAllNotes()
            } else {
                repository.searchNotes(query.trim())
            }
            _notes.value = result
            _isLoading.value = false
        }
    }

    fun action(action: HomeAction) {
        when (action) {
            is HomeAction.Delete -> {
                onDeleteNote(action.note)
            }
            is HomeAction.TogglePin -> {
                onTogglePin(action.note)
            }
            is HomeAction.SearchQueryChange -> {
                onSearchQueryChange(action.query)
            }
        }
    }

    private fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        loadNotes()
    }

    private fun onDeleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
            loadNotes()
        }
    }

    private fun onTogglePin(note: Note) {
        viewModelScope.launch {
            repository.togglePin(note.id, !note.isPinned)
            loadNotes()
        }
    }
}
