package com.example.uiharness.diary

// Bir günlük kaydı. Tarih epoch milisaniye olarak tutulur, biçimlendirme ekranda yapılır.
data class DiaryEntry(
    val id: Long,
    val createdAtMillis: Long,
    val title: String,
    val body: String,
)

// Veri kaynağı sözleşmesi. Bugün bellek içi, ileride kalıcı depo bu arayüzü uygular.
interface DiaryRepository {
    fun all(): List<DiaryEntry>
    fun create(createdAtMillis: Long): DiaryEntry
    fun save(entry: DiaryEntry): DiaryEntry
    fun delete(id: Long)
}
