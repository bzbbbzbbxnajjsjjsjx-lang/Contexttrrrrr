plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Lint API AGP'nin kendi lint çalışma zamanıyla eşleşmeli, bu yüzden compileOnly.
    compileOnly(libs.lint.api)
    compileOnly(libs.lint.checks)
}

// AGP, kuralları bu manifest girdisinden bulur.
tasks.jar {
    manifest {
        attributes("Lint-Registry-v2" to "com.example.uiharness.lint.UiHarnessIssueRegistry")
    }
}
