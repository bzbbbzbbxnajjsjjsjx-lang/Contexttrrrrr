# Sürüm kayıtları

Tarih: 2026-10-26. Tüm sürümler `gradle/libs.versions.toml` ve `gradle/wrapper/gradle-wrapper.properties` içinde sabit.
Dinamik sürüm (`+`, `latest`) yok.

| Bileşen | Seçilen | Kaynak | Not |
|---|---|---|---|
| JDK (hedef) | 25.0.5 LTS (Temurin) | Adoptium binary | Kullanıcı kararı: JDK 25. `jvmToolchain(25)`. |
| Gradle | 9.8.2 | https://gradle.org/releases/ | En güncel kararlı, 19 Eki 2026. |
| Android Gradle Plugin | 9.4.1 | https://developer.android.com/build/releases/gradle-plugin | Lint uyarısıyla güncellendi (9.4.0 -> 9.4.1). Min. Gradle 9.6.0, min. JDK 17. Max. API 37. |
| Kotlin | 2.4.21 | https://kotlinlang.org/docs/releases.html | En güncel kararlı, 8 Eki 2026. |
| Compose BOM | 2026.09.00 | https://developer.android.com/develop/ui/compose/bom/bom-mapping | Kararlılık durumu sayfada teyit edilemedi, derleme ile doğrulanacak. |
| compose material3 | 1.4.0 | BOM eşlemesi | BOM tarafından yönetilir, ayrıca sabitlenmez. |
| compose ui | 1.12.1 | BOM eşlemesi | BOM tarafından yönetilir. |
| activity-compose | 1.13.0 | https://developer.android.com/jetpack/androidx/releases/activity | 11 Mart 2026, kararlı. |
| lifecycle (runtime-compose, viewmodel-compose) | 2.11.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle | 17 Haziran 2026, kararlı. |
| compileSdk | 37.2 | https://developer.android.com/tools/releases/platforms | Compose BOM 2026.09.00 kütüphaneleri compileSdk 37+ istiyor (assembleDebug hatası). Kullanıcı onayı ile 37.2. |
| targetSdk | 37 | https://developer.android.com/tools/releases/platforms | Lint "en güncel hedef" uyarısıyla 37'ye yükseltildi. API 37'nin kararlılığı kaynakta net değil, derleme ve lint GEÇTİ. |
| minSdk | 24 | Harness kararı | Kaynak yok, proje gereksinimi gelince değiştirilir. |

compileSdk, targetSdk ve minSdk değerleri `libs.versions.toml` içindedir.

## Uyumluluk notları

- **AGP 9.x ile built-in Kotlin:** `org.jetbrains.kotlin.android` plugin'i uygulanmaz, AGP 9.0'dan beri gerekmiyor.
  Kaynak: https://developer.android.com/build/migrate-to-built-in-kotlin
- **Compose compiler:** `org.jetbrains.kotlin.plugin.compose`, Kotlin ile aynı sürümde (`kotlin = 2.4.21`).
- **AGP 9.4 ile Kotlin 2.4.21 uyumu:** AGP 9.4 sürüm notları Kotlin sürümünü açıkça belirtmiyor. Derleme ile doğrulanacak.
- **API 37 (Android 17):** Sayfa kararlı mı beta mı olduğunu net söylemiyor. Bu yüzden compileSdk 36'da kaldık.
  AGP 9.4 API 37'ye kadar destekliyor.
- **Screenshot testi (ileride):** Resmi gereksinimler AGP 9.0+, Kotlin 2.2.10+, JDK 17+. Bu projede sağlanıyor.
  Kaynak: https://developer.android.com/studio/preview/compose-screenshot-testing

## Tasarım dili kararı: standart Material 3

Tema standart Material 3 (`MaterialTheme`, material3 1.4.0 hattı). Material 3 Expressive kullanılmıyor.

- Compose'da Expressive API'leri şu an `1.5.0-alpha` serisinde. Son gözlenen sürüm `1.5.0-alpha27`. Alpha olduğu için sürüm kuralımız gereği kullanılmıyor.
- Kaynak: https://developer.android.com/jetpack/androidx/releases/compose-material3
- Kararlı olduğunda geçiş: `ui/theme/Theme.kt` içinde `MaterialTheme` yerine `MaterialExpressiveTheme`, ardından bu sürüm kaydı ve `tokens.md` güncellenir. Diğer dosyalar değişmez, çünkü ekranlar `MaterialTheme.*` tokenlarını kullanıyor.

## Test kütüphaneleri

| Bileşen | Sürüm | Kaynak | Not |
|---|---|---|---|
| Roborazzi (plugin + compose) | 1.77.0 | https://repo1.maven.org/maven2/io/github/takahirom/roborazzi/ | Üçüncü taraf (Google değil). Maven Central'da en güncel kararlı. Kullanıcı onayı ile seçildi. |
| Robolectric | 4.17 | https://repo1.maven.org/maven2/org/robolectric/robolectric/ | Emülatörsüz JVM testi. |
| JUnit | 4.13.2 | https://repo1.maven.org/maven2/junit/junit/ | Robolectric ve Compose test rule için. |
| Lint API (`lint-api`, `lint-checks`) | 32.4.1 | https://dl.google.com/dl/android/maven2/com/android/tools/lint/lint-api/maven-metadata.xml | `lint-rules` modülü için. AGP 9.4 ile aynı minor hattı (32.4). Kuralın AGP ile çalıştığı `lintDebug` ile kanıtlandı. Google, lint API'sinin kararlı olmadığını belirtiyor. |

Resmi Google Compose screenshot aracı (`com.android.compose.screenshot`) hâlâ alpha (`0.0.1-alpha16`). Kararlı olmadığı için kullanılmıyor: https://developer.android.com/studio/preview/compose-screenshot-testing

**Test JVM sürümü:** Robolectric 4.17, JDK 25 üzerinde çalışmıyor. Testler JDK 21 launcher ile koşar, bayt kodu hedefi 21'dir. Gerekli `--add-exports` / `--add-opens` bayrakları `app/build.gradle.kts` içindedir.

**Test SDK:** Robolectric `sdk = 36` ile sabitlenmiş (`ScreenSnapshotTest`). targetSdk 37'de uygulama paylaşımlı bellek yolunda çöküyor.

## Açık kalan kararlar

- Kotlin 2.4.21 + AGP 9.4.0 derlenemezse Kotlin sürümü önce düşürülür, sonra kayıt güncellenir.
- Compose BOM'un kararlılığı derleme sırasında teyit edilir.
- API 37 kararlı olduğunda compileSdk yükseltilir.
