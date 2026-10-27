package com.example.uiharness.notes

class InMemoryNotesRepository(seed: List<Note> = emptyList()) : NotesRepository {

    private val notes = seed.toMutableList()
    private var nextId = (seed.maxOfOrNull { it.id } ?: 0L) + 1L

    // En yeni not üstte.
    override fun all(): List<Note> = notes.sortedByDescending { it.id }

    override fun create(): Note {
        val note = Note(id = nextId++, title = "", body = "")
        notes.add(note)
        return note
    }

    override fun save(note: Note): Note {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index >= 0) notes[index] = note
        return note
    }

    override fun delete(id: Long) {
        notes.removeAll { it.id == id }
    }
}
