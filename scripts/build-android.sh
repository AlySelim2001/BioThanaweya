#!/usr/bin/env bash
# Build optimized Android APK/AAB variants and stage them with SHA-256 checksums.
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ANDROID_DIR="${ROOT_DIR}/android"
OUTPUT_DIR="${ROOT_DIR}/output-binaries"

command -v npm >/dev/null 2>&1 || { echo 'Error: npm is required to build the web assets.' >&2; exit 1; }
command -v java >/dev/null 2>&1 || { echo 'Error: Java 17 is required for the Android build.' >&2; exit 1; }

cd "$ROOT_DIR"
npm run build

if [[ -x "${ANDROID_DIR}/gradlew" ]]; then
    GRADLE_WRAPPER="${ANDROID_DIR}/gradlew"
elif command -v gradle >/dev/null 2>&1; then
    echo 'Gradle wrapper scripts are absent; generating a Gradle 8.7 wrapper.'
    gradle -p "$ANDROID_DIR" wrapper --gradle-version 8.7 --distribution-type bin --stacktrace
    GRADLE_WRAPPER="${ANDROID_DIR}/gradlew"
else
    echo 'Error: provide android/gradlew or install Gradle 8.7+ to generate the wrapper.' >&2
    exit 1
fi

chmod +x "$GRADLE_WRAPPER"
"$GRADLE_WRAPPER" -p "$ANDROID_DIR" --no-daemon --stacktrace assembleDebug assembleRelease bundleRelease

mkdir -p "$OUTPUT_DIR"
find "$OUTPUT_DIR" -maxdepth 1 -type f \( -name 'BioThanaweya-*.apk' -o -name 'BioThanaweya-*.aab' -o -name 'SHA256SUMS.txt' \) -delete

DEBUG_APK="$(find "$ANDROID_DIR/app/build/outputs/apk/debug" -maxdepth 1 -type f -name '*.apk' -print -quit 2>/dev/null || true)"
RELEASE_APK="$(find "$ANDROID_DIR/app/build/outputs/apk/release" -maxdepth 1 -type f -name '*.apk' -print -quit 2>/dev/null || true)"
RELEASE_AAB="$(find "$ANDROID_DIR/app/build/outputs/bundle/release" -maxdepth 1 -type f -name '*.aab' -print -quit 2>/dev/null || true)"

[[ -n "$DEBUG_APK" ]] || { echo 'Error: assembleDebug did not produce an APK.' >&2; exit 1; }
[[ -n "$RELEASE_APK" ]] || { echo 'Error: assembleRelease did not produce an APK.' >&2; exit 1; }
cp "$DEBUG_APK" "$OUTPUT_DIR/BioThanaweya-debug.apk"
cp "$RELEASE_APK" "$OUTPUT_DIR/BioThanaweya-release.apk"
if [[ -n "$RELEASE_AAB" ]]; then
    cp "$RELEASE_AAB" "$OUTPUT_DIR/BioThanaweya-release.aab"
fi

if command -v sha256sum >/dev/null 2>&1; then
    (cd "$OUTPUT_DIR" && sha256sum BioThanaweya-* > SHA256SUMS.txt)
elif command -v shasum >/dev/null 2>&1; then
    (cd "$OUTPUT_DIR" && shasum -a 256 BioThanaweya-* > SHA256SUMS.txt)
else
    echo 'Error: sha256sum or shasum is required to generate artifact checksums.' >&2
    exit 1
fi

printf 'Android artifacts staged in %s\n' "$OUTPUT_DIR"
cat "$OUTPUT_DIR/SHA256SUMS.txt"
