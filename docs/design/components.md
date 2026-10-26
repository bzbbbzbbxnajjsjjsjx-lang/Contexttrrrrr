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
| — | — | — | — | — | — |

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
