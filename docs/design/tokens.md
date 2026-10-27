# Tasarım tokenları

Bu dosya kodla birebir aynı tutulur. Kaynak dosyalar:
`app/src/main/java/com/example/uiharness/ui/theme/`.

Bu tokenlar geçicidir. Material Theme Builder çıktısıyla değiştirilecek.

Doğrulama durumu: Hex değerleri Material 3 baseline paletidir. Resmi M3 sayfası dinamik içerik döndürdüğü için hex değerleri bu oturumda doğrulanamadı. Marka rengi istenirse palet Google'ın Material Color Utilities algoritmasıyla o renkten üretilir. Theme Builder kullanılmıyor.

## Renk rolleri (Material 3)

Kod: `Color.kt` (palet), `Theme.kt` (rol eşlemesi).
Ekranlarda `MaterialTheme.colorScheme.<rol>` dışında renk kullanılmaz.

| Rol | Light | Dark |
|---|---|---|
| primary | `md_light_primary` #6750A4 | `md_dark_primary` #D0BCFF |
| onPrimary | #FFFFFF | #381E72 |
| primaryContainer | #EADDFF | #4F378B |
| onPrimaryContainer | #21005D | #EADDFF |
| secondary | #625B71 | #CCC2DC |
| onSecondary | #FFFFFF | #332D41 |
| secondaryContainer | #E8DEF8 | #4A4458 |
| onSecondaryContainer | #1D192B | #E8DEF8 |
| tertiary | #7D5260 | #EFB8C8 |
| onTertiary | #FFFFFF | #492532 |
| tertiaryContainer | #FFD8E4 | #633B48 |
| onTertiaryContainer | #31111D | #FFD8E4 |
| error | #B3261E | #F2B8B5 |
| onError | #FFFFFF | #601410 |
| errorContainer | #F9DEDC | #8C1D18 |
| onErrorContainer | #410E0B | #F9DEDC |
| background | #FFFBFE | #1C1B1F |
| onBackground | #1C1B1F | #E6E1E5 |
| surface | #FFFBFE | #1C1B1F |
| onSurface | #1C1B1F | #E6E1E5 |
| surfaceVariant | #E7E0EC | #49454F |
| onSurfaceVariant | #49454F | #CAC4D0 |
| outline | #79747E | #938F99 |
| outlineVariant | #CAC4D0 | #49454F |

## Boşluk (Spacing)

Kod: `Spacing.kt`. Ekranlarda yalnızca `Spacing.<ad>` kullanılır.

| Ad | Değer |
|---|---|
| xs | 4 dp |
| sm | 8 dp |
| md | 16 dp |
| lg | 24 dp |
| xl | 32 dp |

## Köşe yarıçapı (Radius)

Kod: `Radius.kt`, `Shape.kt`. Ekranlarda yalnızca `Radius.<ad>` kullanılır.

| Ad | Değer | Shapes slotu |
|---|---|---|
| sm | 8 dp | small |
| md | 12 dp | medium |
| lg | 16 dp | large |

## Tipografi

Kod: `Type.kt`. Geçici olarak Material 3 varsayılan ölçeği (`Typography()`) kullanılıyor.
Ekranlarda `MaterialTheme.typography.<stil>` kullanılır, sabit `TextStyle` yazılmaz.
