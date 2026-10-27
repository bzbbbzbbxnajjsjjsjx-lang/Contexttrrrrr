package com.example.uiharness.notes

data class Note(
    val id: Long,
    val title: String,
    val body: String,
)

// Veri kaynağı sözleşmesi. Bugün bellek içi, ileride kalıcı depo bu arayüzü uygular.
interface NotesRepository {
    fun all(): List<Note>
    fun create(): Note
    fun save(note: Note): Note
    fun delete(id: Long)
}
