# Altın istekler

Harness'in tutarlılığını ölçmek için sabit istekler. Her harness değişikliğinden sonra bu istekler yeniden çalıştırılır ve sonuçlar aynı kurallara uymalıdır.

Ölçülen şey kod yapısıdır: `scripts/golden-check.sh` ile statik kurallar kontrol edilir. Görsel çıktı karşılaştırması (screenshot) bu aşamada yok. Resmi Compose screenshot aracı alpha plugin gerektiriyor, sürüm kuralımız alpha kullanımını yasaklıyor. Kararlı bir sürüm çıktığında eklenecek.

## İstekler

| Kod | İstek | Beklenen dosya |
|---|---|---|
| G1 | Başlık ve kısa açıklama gösteren bir not ekranı yap. Sadece `docs/design/components.md` içindeki bileşenleri kullan. | `NoteScreen.kt` |
| G2 | Ad ve e-posta alanı olan bir kayıt formu ekranı yap. Gönder butonu olsun. | `SignUpScreen.kt` |
| G3 | Öğe listesi gösteren bir ekran yap. Liste boşken boş durum mesajı göstersin. | `ItemListScreen.kt` |

## Her çıktı için beklenenler

1. `scripts/golden-check.sh` GEÇTİ döner. Bu, `@Preview` ve `UiState` kuralları ile tema dışı hardcoded değer olmadığını kontrol eder.
2. Ekran composable'ı `UiState` alır. İş mantığı composable içinde değildir, olaylar lambda ile yukarı gider.
3. Boşluk yalnızca `Spacing`, köşe yalnızca `Radius` ile verilir.
4. `scripts/check-tokens.sh` GEÇTİ döner.
5. Bileşen envanteri henüz boş olduğu için G1-G3 bugün bileşen kuralını doğrulayamaz. Envanter doldukça bu madde eklenecek.

## Nasıl çalışır

```
scripts/golden-check.sh app/src/main/java/com/example/uiharness/ui/<ekran>/<Dosya>.kt
```

Aynı istek üç kez çalıştırılır. Üç sonucun da GEÇTİ olması ve birbirinden farklı olmaması beklenir.
