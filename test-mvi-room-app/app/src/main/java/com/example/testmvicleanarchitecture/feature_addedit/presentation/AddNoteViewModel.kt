package com.example.testmvicleanarchitecture.feature_addedit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testmvicleanarchitecture.feature_addedit.domain.AddEditNoteUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AddNoteViewModel.Factory::class)
class AddNoteViewModel @AssistedInject constructor(
    private val useCase: AddEditNoteUseCase,
    @Assisted private val noteId: Long
) : ViewModel() {

    private val _title = MutableStateFlow("")
    private val _content = MutableStateFlow("")
    private val _colorHex = MutableStateFlow("#F5F5F0")
    private val _isPinned = MutableStateFlow(false)
    private val _createdAt = MutableStateFlow(System.currentTimeMillis())
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<AddNoteUiState> = combine(
        _title,
        _content,
        _colorHex,
        _isPinned,
        combine(_createdAt, _errorMessage) { createdAt, error ->
            createdAt to error
        }
    ) { title, content, color, isPinned, pair ->
        AddNoteUiState(
            id = noteId,
            title = title,
            content = content,
            colorHex = color,
            isPinned = isPinned,
            createdAt = pair.first,
            errorMessage = pair.second
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddNoteUiState(id = noteId)
    )

    init {
        if (noteId != 0L) {
            viewModelScope.launch {
                useCase.getNoteById(noteId)?.let { note ->
                    _title.value = note.title
                    _content.value = note.content
                    _colorHex.value = note.colorHex
                    _isPinned.value = note.isPinned
                    _createdAt.value = note.createdAt
                }
            }
        }
    }

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

    fun saveNote(navigateBack: () -> Unit) {
        val title = _title.value.trim()
        val content = _content.value.trim()

        if (title.isBlank() && content.isBlank()) {
            _errorMessage.value = "Cannot save an empty note"
            return
        }

        viewModelScope.launch {
            useCase.saveNote(
                id = noteId,
                title = title,
                content = content,
                colorHex = _colorHex.value,
                isPinned = _isPinned.value,
                createdAt = _createdAt.value
            )
        }
        navigateBack()
    }

    @AssistedFactory
    interface Factory {
        fun create(noteId: Long): AddNoteViewModel
    }
}
