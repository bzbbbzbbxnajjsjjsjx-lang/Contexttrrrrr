# Bileşen envanteri

Yeni bir composable yazmadan önce bu listeye bakılır. Burada olan bileşen kullanılır.
Burada olmayan bileşen gerekiyorsa ajan önce kullanıcıya sorar, kendi başına eklemez.

Her kayıt için alanlar:
- **Ad**: composable adı ve dosyası
- **Parametreler**: imza
- **Ne zaman kullanılır**: kullanım kuralı
- **Kaynak**: resmi M3 bileşeni veya proje bileşeni
- **Preview**: preview fonksiyonu adı

---

## Envanter

Henüz bileşen yok.

| Ad | Dosya | Parametreler | Ne zaman kullanılır | Kaynak | Preview |
|---|---|---|---|---|---|
| `Button` | M3 | `onClick`, `modifier`, `enabled`, `shape`, `colors`, `content` | Ana eylem: kaydet, gönder | [Buttons](https://developer.android.com/develop/ui/compose/components/button) | — |
| `FilledTonalButton` | M3 | Button ile aynı imza | Önemli ama ana olmayan eylem | [Buttons](https://developer.android.com/develop/ui/compose/components/button) | — |
| `OutlinedButton` | M3 | Button ile aynı imza | İkincil eylem, yanında başka düğme varken: iptal, geri | [Buttons](https://developer.android.com/develop/ui/compose/components/button) | — |
| `TextButton` | M3 | Button ile aynı imza | Düşük öncelik: "detay gör", bağlantı benzeri | [Buttons](https://developer.android.com/develop/ui/compose/components/button) | — |
| `Card` | M3 | `modifier`, `shape`, `colors`, `elevation`, `border`, `content` (tıklanabilir overload'da `onClick`) | Tek bir konuya ait içerik bloğu | [Cards](https://developer.android.com/develop/ui/compose/components/card) | — |
| `ElevatedCard` | M3 | Card ile aynı imza | Arka plandan gölgeyle ayrılması gereken öne çıkan içerik | [Cards](https://developer.android.com/develop/ui/compose/components/card) | — |
| `OutlinedCard` | M3 | Card ile aynı imza, `border` varsayılan | Düşük vurgulu içerik, yoğun kart listeleri | [Cards](https://developer.android.com/develop/ui/compose/components/card) | — |
| `OutlinedTextField` | M3 | `value`, `onValueChange`, `modifier`, `label`, `supportingText`, `isError`, `singleLine` | Metin girişi, hata mesajı gösterilecekse `supportingText` + `isError` | [OutlinedTextField reference](https://developer.android.com/reference/kotlin/androidx/compose/material3/OutlinedTextField.composable) (imza sayfadan tam okunamadı, derleme ile doğrulanır) | — |

Preview sütunu boş bırakıldı: her ekran kendi `@Preview`'ini taşır (bkz. `ui/screens/`).

## Ekran örnekleri

| Ekran | Dosya | UiState | Kullanılan bileşenler |
|---|---|---|---|
| Not | `ui/screens/NoteScreen.kt` | `NoteUiState` | Card, Text |
| Kayıt formu | `ui/screens/SignUpScreen.kt` | `SignUpUiState` | OutlinedTextField, Button |
| Öğe listesi | `ui/screens/ItemListScreen.kt` | `ItemListUiState` | OutlinedCard, LazyColumn, Text |

Bu ekranlar golden istekleri G1-G3'e karşılık gelir (bkz. `docs/design/golden/README.md`).

---

## Bileşen şablonu

### `<AdıComposable>`

- **Dosya:** `ui/components/<AdıComposable>.kt`
- **Parametreler:**
  ```kotlin
  fun AdıComposable(
      // ...
      modifier: Modifier = Modifier,
  )
  ```
- **Ne zaman kullanılır:**
- **Kaynak:** Material 3 `<bileşen>` veya proje içi
- **Preview:** `<AdıComposable>Preview`
