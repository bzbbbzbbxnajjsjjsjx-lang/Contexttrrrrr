package com.example.uiharness.notes

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uiharness.ui.theme.AppTheme

// ViewModel'i not ekranına bağlar.
@Composable
fun NotesRoute(
    viewModel: NotesViewModel = viewModel(),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    NotesScreen(
        state = state,
        onAdd = viewModel::addNote,
        onSelect = viewModel::selectNote,
        onBack = viewModel::clearSelection,
        onTitleChange = viewModel::updateTitle,
        onBodyChange = viewModel::updateBody,
        onDelete = viewModel::deleteSelected,
        modifier = modifier,
    )
}

// Önizleme örnek ViewModel verir. Çalışma zamanında route kendi viewModel() örneğini alır.
@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
private fun NotesRoutePreview() {
    AppTheme {
        NotesRoute(viewModel = NotesViewModel(InMemoryNotesRepository(sampleNotes())))
    }
}
