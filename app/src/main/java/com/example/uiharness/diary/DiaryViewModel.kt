package com.example.uiharness.diary

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Günlük iş mantığı: ekleme, seçme, düzenleme, silme. Saat dışarıdan verilir, test edilebilir kalır.
class DiaryViewModel(
    private val repository: DiaryRepository = InMemoryDiaryRepository(),
    private val clock: () -> Long = System::currentTimeMillis,
) : ViewModel() {

    private val _state = MutableStateFlow(DiaryUiState(entries = repository.all()))
    val state: StateFlow<DiaryUiState> = _state.asStateFlow()

    fun addEntry() {
        val entry = repository.create(clock())
        _state.value = DiaryUiState(entries = repository.all(), selectedEntry = entry)
    }

    fun selectEntry(id: Long) {
        _state.update { current -> current.copy(selectedEntry = current.entries.firstOrNull { it.id == id }) }
    }

    fun clearSelection() {
        _state.update { current -> current.copy(selectedEntry = null) }
    }

    fun updateTitle(title: String) = editSelected { it.copy(title = title) }

    fun updateBody(body: String) = editSelected { it.copy(body = body) }

    fun deleteSelected() {
        val id = _state.value.selectedEntry?.id ?: return
        repository.delete(id)
        _state.value = DiaryUiState(entries = repository.all(), selectedEntry = null)
    }

    private fun editSelected(transform: (DiaryEntry) -> DiaryEntry) {
        val current = _state.value.selectedEntry ?: return
        val saved = repository.save(transform(current))
        _state.value = DiaryUiState(entries = repository.all(), selectedEntry = saved)
    }
}
