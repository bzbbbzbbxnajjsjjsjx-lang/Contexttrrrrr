package com.example.uiharness.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Kayıt formunun iş mantığı: alan değerleri, e-posta doğrulaması ve gönderme izni.
class SignUpViewModel : ViewModel() {

    private val _state = MutableStateFlow(SignUpUiState())
    val state: StateFlow<SignUpUiState> = _state.asStateFlow()

    fun onNameChange(name: String) {
        _state.update { current -> current.copy(name = name).validated() }
    }

    fun onEmailChange(email: String) {
        _state.update { current -> current.copy(email = email).validated() }
    }
}

private val EMAIL_PATTERN = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

private fun SignUpUiState.validated(): SignUpUiState {
    val emailError = when {
        email.isEmpty() -> null
        EMAIL_PATTERN.matches(email) -> null
        else -> "Geçersiz e-posta"
    }
    val canSubmit = name.isNotBlank() && email.isNotBlank() && emailError == null
    return copy(emailError = emailError, canSubmit = canSubmit)
}
