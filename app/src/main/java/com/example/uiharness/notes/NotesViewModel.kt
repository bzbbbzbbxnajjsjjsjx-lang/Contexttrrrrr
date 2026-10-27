package com.example.uiharness.notes

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Notların iş mantığı: ekleme, seçme, düzenleme, silme. Durum tek yönlü akar.
class NotesViewModel(
    private val repository: NotesRepository = InMemoryNotesRepository(),
) : ViewModel() {

    private val _state = MutableStateFlow(NotesUiState(notes = repository.all()))
    val state: StateFlow<NotesUiState> = _state.asStateFlow()

    fun addNote() {
        val note = repository.create()
        _state.value = NotesUiState(notes = repository.all(), selectedNote = note)
    }

    fun selectNote(id: Long) {
        _state.update { current -> current.copy(selectedNote = current.notes.firstOrNull { it.id == id }) }
    }

    fun clearSelection() {
        _state.update { current -> current.copy(selectedNote = null) }
    }

    fun updateTitle(title: String) = editSelected { it.copy(title = title) }

    fun updateBody(body: String) = editSelected { it.copy(body = body) }

    fun deleteSelected() {
        val id = _state.value.selectedNote?.id ?: return
        repository.delete(id)
        _state.value = NotesUiState(notes = repository.all(), selectedNote = null)
    }

    private fun editSelected(transform: (Note) -> Note) {
        val current = _state.value.selectedNote ?: return
        val saved = repository.save(transform(current))
        _state.value = NotesUiState(notes = repository.all(), selectedNote = saved)
    }
}
