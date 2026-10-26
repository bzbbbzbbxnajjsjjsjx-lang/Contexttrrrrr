#!/usr/bin/env bash
# Token kontrolü:
#  1) docs/design/tokens.md ile ui/theme Kotlin değerleri birebir aynı mı
#  2) ui/theme dışında hardcoded Color, hex renk veya dp değeri var mı
# Çıkış: 0 GEÇTİ, 1 KALDI. Her sorun bir satır olarak yazılır.
set -uo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SRC="$ROOT/app/src/main/java"
THEME="$SRC/com/example/uiharness/ui/theme"
TOKENS="$ROOT/docs/design/tokens.md"
problems=0

fail() {
    echo "KALDI: $1"
    problems=$((problems + 1))
}

# --- 1a) Spacing / Radius: md tablosu ile Kotlin aynı mı -------------------
# Kotlin: `val xs: Dp = 4.dp` satırlarından "ad değer" üretir.
kt_dp() {
    grep -hE '^\s*val [a-z]+: Dp = [0-9]+\.dp' "$1" |
        sed -E 's/^\s*val ([a-z]+): Dp = ([0-9]+)\.dp.*/\1 \2/'
}
# Markdown: bölüm başlığına göre "| ad | 4 dp |" satırlarını alır.
md_dp() {
    awk -v section="$1" '
        /^## / { in_section = index($0, section) > 0; next }
        in_section && /^\| [a-z]+ \| [0-9]+ dp \|/ {
            gsub(/[|]/, " "); print $1, $2
        }' "$TOKENS"
}

diff_dp() {
    local name="$1" kt_file="$2" section="$3"
    local kt md
    kt="$(kt_dp "$kt_file" | sort)"
    md="$(md_dp "$section" | sort)"
    if [[ -z "$md" ]]; then
        fail "$name: tokens.md içinde '$section' bölümünde değer bulunamadı"
        return
    fi
    if [[ "$kt" != "$md" ]]; then
        fail "$name: tokens.md ile Kotlin farklı"
        diff <(echo "$md") <(echo "$kt") | sed 's/^/    /'
    fi
}

diff_dp "Spacing" "$THEME/Spacing.kt" "Boşluk"
diff_dp "Radius" "$THEME/Radius.kt" "Köşe"

# --- 1b) Renkler: tokens.md'deki her hex, Color.kt içinde olmalı ve tersi ---
md_hex="$(grep -oE '#[0-9A-Fa-f]{6}' "$TOKENS" | tr -d '#' | tr 'a-f' 'A-F' | sort -u)"
kt_hex="$(grep -oE '0xFF[0-9A-Fa-f]{6}' "$THEME/Color.kt" | sed 's/^0xFF//' | tr 'a-f' 'A-F' | sort -u)"

while read -r hex; do
    [[ -z "$hex" ]] && continue
    grep -qx "$hex" <<<"$kt_hex" || fail "Renk tokens.md'de var, Color.kt'de yok: #$hex"
done <<<"$md_hex"

while read -r hex; do
    [[ -z "$hex" ]] && continue
    grep -qx "$hex" <<<"$md_hex" || fail "Renk Color.kt'de var, tokens.md'de yok: #$hex"
done <<<"$kt_hex"

# --- 2) Tema dışında hardcoded değer -----------------------------------
# Renk: Color(...) çağrısı, 0xFF... hex literal, Color.Red gibi sabitler.
color_hits="$(grep -rnE 'Color\(|0x[0-9A-Fa-f]{6,8}|Color\.(Red|Blue|Green|Black|White|Gray|Yellow|Cyan|Magenta|Transparent)\b' "$SRC" --include='*.kt' |
    grep -v "^$THEME/" || true)"
if [[ -n "$color_hits" ]]; then
    fail "ui/theme dışında hardcoded renk"
    sed 's/^/    /' <<<"$color_hits"
fi

# dp: sayısal dp literali (ör. 16.dp, 8.5.dp). Tema dışında yasak.
dp_hits="$(grep -rnE '[0-9]+(\.[0-9]+)?\.dp\b' "$SRC" --include='*.kt' |
    grep -v "^$THEME/" || true)"
if [[ -n "$dp_hits" ]]; then
    fail "ui/theme dışında hardcoded dp"
    sed 's/^/    /' <<<"$dp_hits"
fi

if (( problems > 0 )); then
    echo "SONUÇ: KALDI ($problems sorun)"
    exit 1
fi
echo "SONUÇ: GEÇTİ"
