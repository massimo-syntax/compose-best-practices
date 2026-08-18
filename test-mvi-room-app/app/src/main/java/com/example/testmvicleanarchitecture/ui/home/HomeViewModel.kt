package com.example.testmvicleanarchitecture.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testmvicleanarchitecture.data.local.entity.NoteEntity
import com.example.testmvicleanarchitecture.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _recentlyDeletedNote = MutableStateFlow<NoteEntity?>(null)
    private val _userMessage = MutableStateFlow<String?>(null)

    /**
     * Reactive notes stream from repository.
     * Switches between search and full list dynamically based on search query Flow.
     */
    private val _notesFlow = _searchQuery.flatMapLatest { query ->
        if (query.isBlank()) {
            repository.getAllNotes()
        } else {
            repository.searchNotes(query.trim())
        }
    }

    /**
     * Main StateFlow exposed to Compose via collectAsStateWithLifecycle().
     * Uses SharingStarted.WhileSubscribed(5_000) to stop upstream collection when app is in background.
     */
    val uiState: StateFlow<HomeUiState> = combine(
        _notesFlow,
        _searchQuery,
        _userMessage
    ) { notes, query, message ->
        HomeUiState(
            notes = notes,
            searchQuery = query,
            isLoading = false,
            userMessage = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onDeleteNote(note: NoteEntity) {
        viewModelScope.launch {
            _recentlyDeletedNote.value = note
            repository.deleteNote(note)
            _userMessage.value = "Note deleted"
        }
    }

    fun undoDelete() {
        val noteToRestore = _recentlyDeletedNote.value ?: return
        viewModelScope.launch {
            repository.saveNote(noteToRestore.copy(id = 0L))
            _recentlyDeletedNote.value = null
            _userMessage.value = "Note restored"
        }
    }

    fun onTogglePin(note: NoteEntity) {
        viewModelScope.launch {
            repository.togglePin(note.id, !note.isPinned)
        }
    }

    fun userMessageShown() {
        _userMessage.value = null
    }
}
