plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.example.uiharness"

    compileSdk {
        version = release(libs.versions.compileSdk.get().toInt()) {
            minorApiLevel = libs.versions.compileSdkMinor.get().toInt()
        }
    }

    defaultConfig {
        applicationId = "com.example.uiharness"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "0.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
    }

    testOptions {
        unitTests {
            // Robolectric'in Compose kaynaklarını ve fontları görmesi için gerekli.
            isIncludeAndroidResources = true
        }
    }
}

// Derleme JDK 25 ile yapılır. Bayt kodu hedefi 21: Robolectric 4.17, JDK 25'te
// çalışmıyor (FileDescriptor hatası), testler JDK 21 ile koşar. Bayt kodu 21 olduğu için
// hem derlenmiş kod hem testler aynı JVM sürümüne uyar.
android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    jvmToolchain(25)
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

// Ekran görüntüsü testleri kayıtlı referansla karşılaştırır (verify modu).
// Referansı güncellemek için bilinçli olarak `recordRoborazziDebug` çalıştırılır.
tasks.withType<Test>().configureEach {
    javaLauncher.set(javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(21)) })
    systemProperty("roborazzi.test.verify", "true")
    // Robolectric, java.io.FileDescriptor alanlarına ve jdk.internal.access.SharedSecrets'e reflection ile erişir.
    jvmArgs(
        "--add-opens", "java.base/java.io=ALL-UNNAMED",
        "--add-exports", "java.base/jdk.internal.access=ALL-UNNAMED",
    )
}

dependencies {
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    lintChecks(project(":lint-rules"))
}
