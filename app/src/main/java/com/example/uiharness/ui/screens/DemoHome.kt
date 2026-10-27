package com.example.uiharness.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.uiharness.ui.theme.AppTheme
import com.example.uiharness.ui.theme.Spacing

// Uygulamanın giriş noktası: ekran seçici. Seçilen ekran route'u açılır.
enum class DemoDestination(val label: String) {
    NOTE("Not"),
    SIGN_UP("Kayıt formu"),
    ITEM_LIST("Öğe listesi"),
}

@Composable
fun DemoHome(
    modifier: Modifier = Modifier,
) {
    var destination by rememberSaveable { mutableStateOf<DemoDestination?>(null) }

    Column(
        modifier = modifier.fillMaxSize().padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        val current = destination
        if (current == null) {
            Text(text = "Ekran seç", style = MaterialTheme.typography.titleLarge)
            DemoDestination.entries.forEach { item ->
                OutlinedButton(
                    onClick = { destination = item },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(item.label)
                }
            }
        } else {
            TextButton(onClick = { destination = null }) {
                Text("Geri")
            }
            when (current) {
                DemoDestination.NOTE -> NoteScreen(
                    state = NoteUiState(title = "Alışveriş", body = "Süt, ekmek, yumurta"),
                )
                DemoDestination.SIGN_UP -> SignUpRoute()
                DemoDestination.ITEM_LIST -> ItemListRoute()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DemoHomePreview() {
    AppTheme {
        DemoHome()
    }
}
