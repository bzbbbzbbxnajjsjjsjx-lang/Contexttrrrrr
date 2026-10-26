#!/usr/bin/env bash
# Altın kontrol: üretilen Kotlin dosyalarının statik kurallara uyup uymadığını ölçer.
# Aynı istek tekrar tekrar üretildiğinde sonuç da aynı kurallara uymalı.
#
# Kullanım: scripts/golden-check.sh <dosya.kt> [dosya.kt ...]
# Çıkış: 0 hepsi GEÇTİ, 1 en az biri KALDI, 2 kullanım hatası.
set -uo pipefail

if [[ $# -lt 1 ]]; then
    echo "kullanım: golden-check.sh <dosya.kt> [...]" >&2
    exit 2
fi

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
THEME_DIR="$ROOT/app/src/main/java/com/example/uiharness/ui/theme"
failed=0

check_file() {
    local file="$1" problems=0
    local name
    name="$(basename "$file")"

    if [[ ! -f "$file" ]]; then
        echo "KALDI $name: dosya yok"
        return 1
    fi

    # 1) Composable varsa @Preview de olmalı.
    if grep -q '@Composable' "$file" && ! grep -q '@Preview' "$file"; then
        echo "KALDI $name: @Composable var, @Preview yok"
        problems=$((problems + 1))
    fi

    # 2) Ekran composable'ı (adı Screen ile biter) UiState almalı.
    # Ekran tanımı: "fun <Ad>Screen(" satırı. Parametre listesi çok satırlı olabilir,
    # bu yüzden tanımdan sonraki ilk ")" satırına kadar bakılır.
    while IFS= read -r line; do
        local screen
        screen="$(sed -E 's/.*fun ([A-Za-z0-9_]+Screen)\(.*/\1/' <<<"$line")"
        if ! awk -v pat="fun $screen(" '
                index($0, pat) { on = 1 }
                on { buf = buf $0 "\n" }
                on && /\)[ ]*\{|\) *\{|\)$/ { exit }
                END { print buf }' "$file" | grep -q 'UiState'; then
            echo "KALDI $name: $screen UiState parametresi almıyor"
            problems=$((problems + 1))
        fi
    done < <(grep -E 'fun [A-Za-z0-9_]+Screen\(' "$file")

    # 3) Tema dışında hardcoded renk veya dp yok (check-tokens ile aynı kural).
    if [[ "$file" != "$THEME_DIR"/* ]]; then
        local hits
        hits="$(grep -nE 'Color\(|0x[0-9A-Fa-f]{6,8}|[0-9]+(\.[0-9]+)?\.dp\b' "$file" || true)"
        if [[ -n "$hits" ]]; then
            echo "KALDI $name: hardcoded renk/dp"
            sed 's/^/    /' <<<"$hits"
            problems=$((problems + 1))
        fi
    fi

    if (( problems > 0 )); then
        return 1
    fi
    echo "GEÇTİ $name"
    return 0
}

for f in "$@"; do
    check_file "$f" || failed=$((failed + 1))
done

if (( failed > 0 )); then
    echo "SONUÇ: KALDI ($failed dosya)"
    exit 1
fi
echo "SONUÇ: GEÇTİ"
