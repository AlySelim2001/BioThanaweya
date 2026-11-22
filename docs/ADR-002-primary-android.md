# ADR-002: Primary Android Delivery = Capacitor Shell

## Context
The repository contains three Android-related application IDs and delivery stacks:
- `com.aistudio.biothanaweya.xmptqr` in `android/app/build.gradle` (native AGP module)
- `com.aistudio.biothanaweya.xmptqr` in `capacitor.config.ts` (Capacitor config)
- `com.aistudio.biothanaweya.exam` in `android/app/src/main/assets/capacitor.config.json` (asset-level config, conflicting)
- Flutter module in `lib/` with no native Android platform — unbuildable to APK
- Web/PWA in `src/` with confirmed `npm ci ✅`, `tsc --noEmit ✅`, `vitest ✅`, `npm run build ✅`

The Capacitor shell (`android/`) wraps the Web Vite bundle (produced by `npm run build` with `CAPACITOR_BUILD=true`) and produces a working debug APK via `./gradlew assembleDebug`. The Flutter module has no `android/` directory and cannot be built.

## Decision
**The official Android application is the Capacitor shell** (`android/` directory), because:
1. It is the only Android-delivery stack that is confirmed buildable and tested (`npm run build` → `cap sync android` → `./gradlew assembleDebug` all succeed).
2. It wraps the Web/PWA bundle which has passing tests and type checks, ensuring the Web experience is primary and the APK is a faithful shell.
3. The Flutter module (`lib/`) lacks native Android platform files and cannot produce an APK without significant additional work.
4. Having the Capacitor shell as the primary delivery mechanism aligns with the repository's verified CI pipeline (`.github/workflows/android-apk-build.yml`).

## Consequences
- **Positive**: Single, unambiguous delivery path; the Web/PWA stack remains the source of truth; the APK is generated from tested, linted code; clear upgrade path if native features are needed later (by ejecting from Capacitor or adding platform-specific code).
- **Negative**: The native `android/app/` AGP module (with `namespace 'com.biothanaweya.app'` and `applicationId 'com.biothanaweya.app'`) is now secondary; developers needing native-only features must work within the Capacitor workflow or extract the AGP module.
- **Alternative rejected**: Making the native AGP module primary would require removing Capacitor integration, losing the verified Web-to-APK pipeline, and maintaining a second, untested build path. Deleting the Capacitor config would break the WebView-based delivery that the Android build currently relies on.

## Alternatives Rejected
1. **Flutter as primary Android** — Flutter has no `android/` directory and no verified build path; the Capacitor stack is the only one with a passing CI pipeline.
2. **Native AGP as primary, Capacitor as secondary** — Would discard the only build pipeline that actually succeeds (`npm run build && cap sync android && ./gradlew assembleDebug`), reverting to a manual, unverified process.
3. **Multiple applicationIDs** — Contravenes the gate requirement that `grep -rn "applicationId" android/ capacitor.config.ts` returns one value only; creates ambiguity for users and the Play Store.

---
*Decision: Capacitor shell is the primary Android delivery mechanism because it is the only stack confirmed buildable and tested through the CI pipeline.*