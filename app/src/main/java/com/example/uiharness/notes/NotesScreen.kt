package com.example.uiharness.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.uiharness.ui.theme.AppTheme
import com.example.uiharness.ui.theme.Spacing

// Not ekranı: liste ve düzenleme görünümü, duruma göre seçilir. Değişiklikler anında kaydedilir.
@Composable
fun NotesScreen(
    state: NotesUiState,
    onAdd: () -> Unit,
    onSelect: (Long) -> Unit,
    onBack: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selected = state.selectedNote
    if (selected == null) {
        NoteList(
            state = state,
            onAdd = onAdd,
            onSelect = onSelect,
            modifier = modifier,
        )
    } else {
        NoteEditor(
            note = selected,
            onBack = onBack,
            onTitleChange = onTitleChange,
            onBodyChange = onBodyChange,
            onDelete = onDelete,
            modifier = modifier,
        )
    }
}

@Composable
private fun NoteList(
    state: NotesUiState,
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
        Text(text = "Notlar", style = MaterialTheme.typography.headlineMedium)
        FilledTonalButton(onClick = onAdd, modifier = Modifier.fillMaxWidth()) {
            Text("Yeni not")
        }
        if (state.notes.isEmpty()) {
            Text(
                text = "Henüz not yok",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                items(state.notes, key = { it.id }) { note ->
                    Card(
                        onClick = { onSelect(note.id) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(Spacing.md),
                            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                        ) {
                            Text(
                                text = note.title.ifBlank { "Başlıksız not" },
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            if (note.body.isNotBlank()) {
                                Text(
                                    text = note.body,
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
private fun NoteEditor(
    note: Note,
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
        OutlinedTextField(
            value = note.title,
            onValueChange = onTitleChange,
            label = { Text("Başlık") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = note.body,
            onValueChange = onBodyChange,
            label = { Text("İçerik") },
            minLines = 6,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotesScreenListPreview() {
    AppTheme {
        NotesScreen(
            state = NotesUiState(notes = sampleNotes()),
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
private fun NotesScreenEditorPreview() {
    AppTheme {
        NotesScreen(
            state = NotesUiState(notes = sampleNotes(), selectedNote = sampleNotes().first()),
            onAdd = {},
            onSelect = {},
            onBack = {},
            onTitleChange = {},
            onBodyChange = {},
            onDelete = {},
        )
    }
}

fun sampleNotes(): List<Note> = listOf(
    Note(id = 3, title = "Alışveriş", body = "Süt, ekmek, yumurta"),
    Note(id = 2, title = "Toplantı notları", body = "Proje takvimi güncellendi. Salı günü demo var."),
    Note(id = 1, title = "", body = "Kitap listesi"),
)
