package com.example.uiharness.ui.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uiharness.ui.theme.AppTheme

// ViewModel'i ekrana bağlar. Ekran durumsuz kalır, durumu buradan alır.
@Composable
fun SignUpRoute(
    viewModel: SignUpViewModel = viewModel(),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    SignUpScreen(
        state = state,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        // Gönderme henüz iş mantığında tanımlı değil. Eklendiğinde ViewModel'e taşınır.
        onSubmit = {},
        modifier = modifier,
    )
}

// Önizleme örnek ViewModel verir. Çalışma zamanında route kendi viewModel() örneğini alır.
@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
private fun SignUpRoutePreview() {
    AppTheme {
        SignUpRoute(viewModel = SignUpViewModel())
    }
}
