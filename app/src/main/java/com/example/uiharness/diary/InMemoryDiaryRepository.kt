package com.example.uiharness.diary

class InMemoryDiaryRepository(seed: List<DiaryEntry> = emptyList()) : DiaryRepository {

    private val entries = seed.toMutableList()
    private var nextId = (seed.maxOfOrNull { it.id } ?: 0L) + 1L

    // En yeni kayıt üstte.
    override fun all(): List<DiaryEntry> = entries.sortedByDescending { it.createdAtMillis }

    override fun create(createdAtMillis: Long): DiaryEntry {
        val entry = DiaryEntry(id = nextId++, createdAtMillis = createdAtMillis, title = "", body = "")
        entries.add(entry)
        return entry
    }

    override fun save(entry: DiaryEntry): DiaryEntry {
        val index = entries.indexOfFirst { it.id == entry.id }
        if (index >= 0) entries[index] = entry
        return entry
    }

    override fun delete(id: Long) {
        entries.removeAll { it.id == id }
    }
}
