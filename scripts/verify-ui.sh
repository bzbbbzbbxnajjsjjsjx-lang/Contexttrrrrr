#!/usr/bin/env bash
# Doğrulama kapısı: assembleDebug -> testDebugUnitTest -> lintDebug.
# Biri kalırsa durur, adımı ve nedeni yazar, tam logu dosyaya bırakır.
set -uo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT" || exit 1

LOG_DIR="${VERIFY_LOG_DIR:-$ROOT/build/verify-logs}"
mkdir -p "$LOG_DIR"

STEPS=(assembleDebug testDebugUnitTest lintDebug)

for step in "${STEPS[@]}"; do
    log="$LOG_DIR/$step.log"
    echo "==> $step"
    if ./gradlew "$step" --console=plain >"$log" 2>&1; then
        echo "    OK"
        continue
    fi

    echo "    KALDI: $step"
    echo "    Log: $log"
    reason="$(grep -m3 -E '^e: |What went wrong|FAILED|Lint found errors' -A2 "$log" | head -n 8)"
    if [[ -n "$reason" ]]; then
        echo "    Neden:"
        sed 's/^/      /' <<<"$reason"
    fi
    echo "--- Son 20 satır ---"
    tail -n 20 "$log"
    echo "SONUÇ: KALDI"
    exit 1
done

echo "SONUÇ: GEÇTİ"
