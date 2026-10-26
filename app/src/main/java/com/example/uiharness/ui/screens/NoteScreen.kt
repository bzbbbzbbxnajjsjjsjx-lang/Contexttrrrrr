package com.example.uiharness.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.uiharness.ui.theme.AppTheme
import com.example.uiharness.ui.theme.Spacing

data class NoteUiState(
    val title: String,
    val body: String,
)

@Composable
fun NoteScreen(
    state: NoteUiState,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth().padding(Spacing.md)) {
        Column(modifier = Modifier.padding(Spacing.md)) {
            Text(text = state.title, style = MaterialTheme.typography.titleLarge)
            Text(text = state.body, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteScreenPreview() {
    AppTheme {
        NoteScreen(state = NoteUiState(title = "Alışveriş", body = "Süt, ekmek, yumurta"))
    }
}
