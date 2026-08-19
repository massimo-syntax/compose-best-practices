package com.example.testmvicleanarchitecture.ui.add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testmvicleanarchitecture.data.local.entity.NoteEntity
import com.example.testmvicleanarchitecture.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentNoteId: Long = savedStateHandle.get<Long>("noteId") ?: 0L

    private val _title = MutableStateFlow("")
    private val _content = MutableStateFlow("")
    private val _colorHex = MutableStateFlow("#F5F5F0")
    private val _isPinned = MutableStateFlow(false)
    private val _createdAt = MutableStateFlow(System.currentTimeMillis())

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    sealed interface UiEvent {
        data object SaveNoteSuccess : UiEvent
        data class ShowSnackbar(val message: String) : UiEvent
    }

    /**
     * Unified StateFlow stream feeding the Compose UI via collectAsStateWithLifecycle().
     */
    val uiState: StateFlow<AddNoteUiState> = combine(
        _title,
        _content,
        _colorHex,
        _isPinned,
        _createdAt
    ) { title, content, color, isPinned, createdAt ->
        AddNoteUiState(
            id = currentNoteId,
            title = title,
            content = content,
            colorHex = color,
            isPinned = isPinned,
            createdAt = createdAt
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddNoteUiState(id = currentNoteId)
    )

    init {
        if (currentNoteId > 0L) {
            loadNote(currentNoteId)
        }
    }

    fun loadNote(id: Long) {
        currentNoteId = id
        if (id == 0L) {
            _title.value = ""
            _content.value = ""
            _colorHex.value = "#F5F5F0"
            _isPinned.value = false
            _createdAt.value = System.currentTimeMillis()
            return
        }
        viewModelScope.launch {
            repository.getNoteById(id)?.let { note ->
                _title.value = note.title
                _content.value = note.content
                _colorHex.value = note.colorHex
                _isPinned.value = note.isPinned
                _createdAt.value = note.createdAt
            }
        }
    }

    // Input state updater functions (Flow updaters)
    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onContentChange(newContent: String) {
        _content.value = newContent
    }

    fun onColorChange(newColorHex: String) {
        _colorHex.value = newColorHex
    }

    fun onTogglePin() {
        _isPinned.value = !_isPinned.value
    }

    /**
     * Saves or updates the note to Room through the repository.
     * Emits SaveNoteSuccess which tells the Navigation 3 backstack to pop to HomeScreenKey.
     */
    fun saveNote() {
        val currentTitle = _title.value.trim()
        val currentContent = _content.value.trim()

        if (currentTitle.isBlank() && currentContent.isBlank()) {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowSnackbar("Cannot save an empty note"))
            }
            return
        }

        viewModelScope.launch {
            val note = NoteEntity(
                id = currentNoteId,
                title = currentTitle.ifBlank { "Untitled Note" },
                content = currentContent,
                createdAt = if (currentNoteId > 0L) _createdAt.value else System.currentTimeMillis(),
                colorHex = _colorHex.value,
                isPinned = _isPinned.value
            )
            repository.saveNote(note)
            // Emit success event -> Navigation 3 pops back to HomeScreenKey
            _eventFlow.emit(UiEvent.SaveNoteSuccess)
        }
    }
}
