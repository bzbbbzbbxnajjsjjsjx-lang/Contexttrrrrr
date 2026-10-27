package com.example.uiharness.diary

// Ekran durumu. selectedEntry doluysa düzenleme görünümü açık, boşsa liste görünümü.
data class DiaryUiState(
    val entries: List<DiaryEntry>,
    val selectedEntry: DiaryEntry? = null,
)
