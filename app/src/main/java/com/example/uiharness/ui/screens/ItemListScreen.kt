package com.example.uiharness.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.uiharness.ui.theme.AppTheme
import com.example.uiharness.ui.theme.Spacing

data class ItemListUiState(
    val items: List<String> = emptyList(),
)

@Composable
fun ItemListScreen(
    state: ItemListUiState,
    modifier: Modifier = Modifier,
) {
    if (state.items.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize().padding(Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Henüz öğe yok", style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            items(state.items) { item ->
                OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                    Text(text = item, modifier = Modifier.padding(Spacing.md))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemListScreenEmptyPreview() {
    AppTheme {
        ItemListScreen(state = ItemListUiState())
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemListScreenFilledPreview() {
    AppTheme {
        ItemListScreen(state = ItemListUiState(items = listOf("Süt", "Ekmek", "Yumurta")))
    }
}
