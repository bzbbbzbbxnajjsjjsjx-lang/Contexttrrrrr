# CLAUDE.md

Bu proje bir Jetpack Compose UI üretim harness'idir. Amaç uygulama değil, AI'ın tutarlı UI üretmesini sağlamaktır.

## Renk ve boşluk

- Renk yalnızca `MaterialTheme.colorScheme` üzerinden kullanılır. Hardcoded `Color` yasak.
- Boşluk yalnızca `Spacing`, köşe yalnızca `Radius` üzerinden verilir. Hardcoded `dp` yasak.
- Tipografi yalnızca `MaterialTheme.typography` üzerinden kullanılır.

## Bileşenler

- Önce `docs/design/components.md` içindeki bileşenler kullanılır.
- Yeni bir bileşen gerekiyorsa önce kullanıcıya sorulur.

## Composable'lar

- Her composable'ın bir `@Preview`'i olur.
- İş mantığı composable'da değil ViewModel'de yazılır. Ekran bir `UiState` alır.

## Tasarım kararları

- `docs/design/tokens.md` ve `docs/design/components.md` dışında tasarım kararı verilmez.
- Belirsizlikte tahmin edilmez, sorulur.

## Sürümler

- Sürümler yalnızca `gradle/libs.versions.toml` içinde yönetilir.
- Dinamik sürüm (`+`, `latest`) kullanılmaz.
- Sürüm değiştirmeden önce kullanıcıya sorulur. Kayıt `docs/design/versions.md` içinde tutulur.

## Doğrulama

- Her değişiklikten sonra `scripts/verify-ui.sh` çalıştırılır.
- "Bitti" kararını yalnızca script verir. Script `GEÇTİ` dönmeden görev tamamlanmış sayılmaz.
- Başarısızlıkta en fazla 3 deneme yapılır, sonra durulup rapor verilir.
- `verify-ui.sh` ve `gradle/libs.versions.toml` değiştirilmeden önce kullanıcıya sorulur.

## Döngü durumu

- Her adım başlamadan `scripts/harness-state.sh start <faz> <adım>` çalıştırılır.
- `verify-ui.sh` sayacı kendisi tutar: başarısızlıkta `fail`, geçişte `pass` çağırır.
- Çıktıda `DUR:` görülürse kod değiştirilmez, durulup rapor verilir.
- Durum `.harness/state.json` içinde tutulur, git'e girmez.

## Altın kontrol

- Yeni üretilen her ekran dosyası `scripts/golden-check.sh <dosya>` ile kontrol edilir.
- Sabit istekler ve beklenenler `docs/design/golden/README.md` içindedir.

## Token kontrolü

- `scripts/check-tokens.sh` tokens.md ile Kotlin değerlerini karşılaştırır ve tema dışı hardcoded `Color` / `dp` arar.
  `verify-ui.sh` bunu ilk adım olarak çalıştırır (`checkTokens`).
- Tasarım tokenı değiştiğinde önce `tokens.md`, sonra `ui/theme` güncellenir, sonra script çalıştırılır.
