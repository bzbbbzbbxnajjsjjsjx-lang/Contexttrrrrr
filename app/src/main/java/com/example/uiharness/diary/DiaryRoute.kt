package com.example.uiharness.diary

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uiharness.ui.theme.AppTheme

// ViewModel'i günlük ekranına bağlar.
@Composable
fun DiaryRoute(
    viewModel: DiaryViewModel = viewModel(),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    DiaryScreen(
        state = state,
        onAdd = viewModel::addEntry,
        onSelect = viewModel::selectEntry,
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
private fun DiaryRoutePreview() {
    AppTheme {
        DiaryRoute(viewModel = DiaryViewModel(InMemoryDiaryRepository(sampleDiaryEntries())))
    }
}
