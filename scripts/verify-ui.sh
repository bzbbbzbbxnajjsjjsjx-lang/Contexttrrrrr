#!/usr/bin/env bash
# Doğrulama kapısı: checkTokens -> assembleDebug -> testDebugUnitTest -> lintDebug.
# Biri kalırsa durur, adımı ve nedeni yazar, tam logu dosyaya bırakır.
# Sonucu .harness/state.json'a da işler (scripts/harness-state.sh).
set -uo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT" || exit 1

LOG_DIR="${VERIFY_LOG_DIR:-$ROOT/build/verify-logs}"
STATE="$ROOT/scripts/harness-state.sh"
mkdir -p "$LOG_DIR"

STEPS=(checkTokens assembleDebug testDebugUnitTest lintDebug)

run_step() {
    local step="$1"
    if [[ "$step" == "checkTokens" ]]; then
        ./scripts/check-tokens.sh
    else
        ./gradlew "$step" --console=plain
    fi
}

fail_step() {
    local step="$1" log="$2" sig reason state_out
    echo "    KALDI: $step"
    echo "    Log: $log"
    reason="$(grep -m3 -E '^e: |What went wrong|FAILED|Lint found errors|KALDI:' -A2 "$log" | head -n 8)"
    if [[ -n "$reason" ]]; then
        echo "    Neden:"
        sed 's/^/      /' <<<"$reason"
    fi
    echo "--- Son 20 satır ---"
    tail -n 20 "$log"

    sig="$("$STATE" sig "$log")"
    state_out="$("$STATE" fail "$sig")"
    echo "--- Döngü durumu ---"
    echo "$state_out"
    echo "SONUÇ: KALDI"
    exit 1
}

for step in "${STEPS[@]}"; do
    log="$LOG_DIR/$step.log"
    echo "==> $step"
    if run_step "$step" >"$log" 2>&1; then
        echo "    OK"
        continue
    fi
    fail_step "$step" "$log"
done

"$STATE" pass >/dev/null
echo "SONUÇ: GEÇTİ"
