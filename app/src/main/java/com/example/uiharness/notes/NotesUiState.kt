package com.example.uiharness.notes

// Ekran durumu. selectedNote doluysa düzenleme görünümü açık, boşsa liste görünümü.
data class NotesUiState(
    val notes: List<Note>,
    val selectedNote: Note? = null,
)
