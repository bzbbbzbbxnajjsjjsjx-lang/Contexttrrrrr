package com.example.uiharness.ui.screens

import com.example.uiharness.ui.theme.AppTheme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

// Her test ekranın bir durumunu PNG olarak yakalar ve kayıtlı referansla karşılaştırır.
// Referans güncellemek için: ./gradlew recordRoborazziDebug
// sdk = 36: targetSdk 37'de Robolectric uygulama paylaşımlı bellek yolunda çöküyor.
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36])
class ScreenSnapshotTest {

    @Test
    fun noteScreen() {
        captureRoboImage("src/test/snapshots/images/noteScreen.png") {
            AppTheme {
                NoteScreen(state = NoteUiState(title = "Alışveriş", body = "Süt, ekmek, yumurta"))
            }
        }
    }

    @Test
    fun signUpScreen_empty() {
        captureRoboImage("src/test/snapshots/images/signUpScreen_empty.png") {
            AppTheme {
                SignUpScreen(
                    state = SignUpUiState(),
                    onNameChange = {},
                    onEmailChange = {},
                    onSubmit = {},
                )
            }
        }
    }

    @Test
    fun signUpScreen_emailError() {
        captureRoboImage("src/test/snapshots/images/signUpScreen_emailError.png") {
            AppTheme {
                SignUpScreen(
                    state = SignUpUiState(name = "Ayşe", email = "ayse@", emailError = "Geçersiz e-posta"),
                    onNameChange = {},
                    onEmailChange = {},
                    onSubmit = {},
                )
            }
        }
    }

    @Test
    fun itemListScreen_empty() {
        captureRoboImage("src/test/snapshots/images/itemListScreen_empty.png") {
            AppTheme {
                ItemListScreen(state = ItemListUiState())
            }
        }
    }

    // Panonun tamamı görünsün diye bu testte ekran daha uzun. Diğer testler varsayılan boyutta kalır.
    @Config(qualifiers = "w360dp-h900dp")
    @Test
    fun healthDashboard() {
        captureRoboImage("src/test/snapshots/images/healthDashboard.png") {
            AppTheme {
                HealthDashboardScreen(
                    state = sampleHealthDashboardState(),
                    onRangeSelected = {},
                )
            }
        }
    }

    @Test
    fun demoHome_menu() {
        captureRoboImage("src/test/snapshots/images/demoHome_menu.png") {
            AppTheme {
                DemoHome()
            }
        }
    }

    @Test
    fun itemListScreen_filled() {
        captureRoboImage("src/test/snapshots/images/itemListScreen_filled.png") {
            AppTheme {
                ItemListScreen(state = ItemListUiState(items = listOf("Süt", "Ekmek", "Yumurta")))
            }
        }
    }
}
