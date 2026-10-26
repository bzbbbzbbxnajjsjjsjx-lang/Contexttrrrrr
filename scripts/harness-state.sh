#!/usr/bin/env bash
# Döngü durumu: context sıfırlansa da adım, deneme sayısı ve son hata kaybolmaz.
# Durum dosyası: .harness/state.json (git dışı).
#
# Kullanım:
#   harness-state.sh show
#   harness-state.sh start <faz> <adım>      yeni adım, sayaçlar sıfırlanır
#   harness-state.sh sig <log dosyası>       hata imzası üretir (adım adı + ilk hata satırı)
#   harness-state.sh fail <imza>             başarısız deneme kaydeder
#   harness-state.sh pass                    adım geçti, sayaçlar sıfırlanır
#
# Çıkış kodları:
#   0  devam edilebilir
#   3  aynı hata iki kez tekrarladı: dur ve raporla
#   4  deneme sınırı (3) doldu: dur ve raporla
set -uo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
STATE_DIR="$ROOT/.harness"
STATE="$STATE_DIR/state.json"
MAX_ATTEMPTS=3

mkdir -p "$STATE_DIR"
[[ -f "$STATE" ]] || echo '{"phase":"","step":"","attempts":0,"lastSignature":"","status":"idle"}' >"$STATE"

get() { jq -r ".$1" "$STATE"; }

write() {
    # $@ = jq ifadesi ve --arg çiftleri
    local tmp
    tmp="$(mktemp "$STATE_DIR/state.XXXXXX")"
    jq "$@" "$STATE" >"$tmp" && mv "$tmp" "$STATE"
}

cmd="${1:-show}"
case "$cmd" in
    show)
        jq . "$STATE"
        ;;

    start)
        [[ $# -eq 3 ]] || { echo "kullanım: start <faz> <adım>" >&2; exit 2; }
        write --arg p "$2" --arg s "$3" \
            '.phase=$p | .step=$s | .attempts=0 | .lastSignature="" | .status="running"'
        jq . "$STATE"
        ;;

    sig)
        [[ $# -eq 2 && -f "$2" ]] || { echo "kullanım: sig <log dosyası>" >&2; exit 2; }
        # Adım adı log dosyasının adından gelir (ör. assembleDebug.log).
        step="$(basename "$2" .log)"
        # İlk gerçek hata satırı: Kotlin "e: ", Gradle "What went wrong" sonrası satır, lint "Error:".
        first="$(grep -m1 -E '^e: |Error:|^> |What went wrong' -A1 "$2" | tr -s ' ' | head -n 2 | tr '\n' ' ')"
        [[ -z "$first" ]] && first="(hata satırı bulunamadı)"
        printf '%s|%s' "$step" "$first" | sha256sum | cut -c1-16
        ;;

    fail)
        [[ $# -eq 2 ]] || { echo "kullanım: fail <imza>" >&2; exit 2; }
        sig="$2"
        attempts=$(( $(get attempts) + 1 ))
        last="$(get lastSignature)"
        write --argjson a "$attempts" --arg g "$sig" \
            '.attempts=$a | .lastSignature=$g | .status="failed"'
        jq . "$STATE"
        if [[ "$last" == "$sig" ]]; then
            echo "DUR: aynı hata iki kez tekrarladı (imza $sig). Kod değiştirmeden raporla."
            exit 3
        fi
        if (( attempts >= MAX_ATTEMPTS )); then
            echo "DUR: $MAX_ATTEMPTS deneme doldu. Raporla."
            exit 4
        fi
        echo "devam: deneme $attempts/$MAX_ATTEMPTS"
        ;;

    pass)
        write '.attempts=0 | .lastSignature="" | .status="passed"'
        jq . "$STATE"
        ;;

    *)
        echo "bilinmeyen komut: $cmd" >&2
        exit 2
        ;;
esac
