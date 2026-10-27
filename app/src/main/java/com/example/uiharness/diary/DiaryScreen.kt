package com.example.uiharness.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.uiharness.ui.theme.AppTheme
import com.example.uiharness.ui.theme.Spacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Günlük ekranı: liste ve düzenleme görünümü, duruma göre seçilir. Değişiklikler anında kaydedilir.
@Composable
fun DiaryScreen(
    state: DiaryUiState,
    onAdd: () -> Unit,
    onSelect: (Long) -> Unit,
    onBack: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selected = state.selectedEntry
    if (selected == null) {
        DiaryList(
            state = state,
            onAdd = onAdd,
            onSelect = onSelect,
            modifier = modifier,
        )
    } else {
        DiaryEditor(
            entry = selected,
            onBack = onBack,
            onTitleChange = onTitleChange,
            onBodyChange = onBodyChange,
            onDelete = onDelete,
            modifier = modifier,
        )
    }
}

@Composable
private fun DiaryList(
    state: DiaryUiState,
    onAdd: () -> Unit,
    onSelect: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Text(text = "Günlük", style = MaterialTheme.typography.headlineMedium)
        FilledTonalButton(onClick = onAdd, modifier = Modifier.fillMaxWidth()) {
            Text("Yeni kayıt")
        }
        if (state.entries.isEmpty()) {
            Text(
                text = "Henüz kayıt yok. Bugün için ilk kaydını ekle.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                items(state.entries, key = { it.id }) { entry ->
                    OutlinedCard(
                        onClick = { onSelect(entry.id) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(Spacing.md),
                            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                        ) {
                            Text(
                                text = formatDate(entry.createdAtMillis),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = entry.title.ifBlank { "Başlıksız kayıt" },
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            if (entry.body.isNotBlank()) {
                                Text(
                                    text = entry.body,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DiaryEditor(
    entry: DiaryEntry,
    onBack: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onBack) { Text("Geri") }
            OutlinedButton(onClick = onDelete) {
                Text("Sil", color = MaterialTheme.colorScheme.error)
            }
        }
        Text(
            text = formatDate(entry.createdAtMillis),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedTextField(
            value = entry.title,
            onValueChange = onTitleChange,
            label = { Text("Başlık") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = entry.body,
            onValueChange = onBodyChange,
            label = { Text("Bugün neler oldu?") },
            minLines = 8,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

// Tarih Türkçe, "20 Ekim 2026, Salı" biçiminde gösterilir.
private fun formatDate(createdAtMillis: Long): String =
    SimpleDateFormat("d MMMM yyyy, EEEE", Locale.forLanguageTag("tr-TR")).format(Date(createdAtMillis))

@Preview(showBackground = true)
@Composable
private fun DiaryScreenListPreview() {
    AppTheme {
        DiaryScreen(
            state = DiaryUiState(entries = sampleDiaryEntries()),
            onAdd = {},
            onSelect = {},
            onBack = {},
            onTitleChange = {},
            onBodyChange = {},
            onDelete = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryScreenEditorPreview() {
    AppTheme {
        DiaryScreen(
            state = DiaryUiState(
                entries = sampleDiaryEntries(),
                selectedEntry = sampleDiaryEntries().first(),
            ),
            onAdd = {},
            onSelect = {},
            onBack = {},
            onTitleChange = {},
            onBodyChange = {},
            onDelete = {},
        )
    }
}

// 2026-10-20 12:00 UTC. Öğe günleri bir gün geriye gider. Öğle saati, saat dilimi farkında tarihi değiştirmez.
private const val SAMPLE_DAY_MILLIS = 1_792_497_600_000L
private const val ONE_DAY_MILLIS = 86_400_000L

fun sampleDiaryEntries(): List<DiaryEntry> = listOf(
    DiaryEntry(
        id = 3,
        createdAtMillis = SAMPLE_DAY_MILLIS,
        title = "Yeni proje",
        body = "Takımla ilk toplantı yaptık. Takvim netleşti, salı günü demo var.",
    ),
    DiaryEntry(
        id = 2,
        createdAtMillis = SAMPLE_DAY_MILLIS - ONE_DAY_MILLIS,
        title = "Yürüyüş",
        body = "Akşam parkta uzun bir yürüyüş yaptım. Hava çok güzeldi.",
    ),
    DiaryEntry(
        id = 1,
        createdAtMillis = SAMPLE_DAY_MILLIS - 2 * ONE_DAY_MILLIS,
        title = "",
        body = "Kitabın ilk bölümünü bitirdim.",
    ),
)
