# ADR-001: Unify applicationId to com.aistudio.biothanaweya.xmptqr

## Context
The repository contains the application ID `com.aistudio.biothanaweya.xmptqr` in multiple locations with one conflict:
- `android/app/build.gradle:7,11` — `namespace 'com.aistudio.biothanaweya.xmptqr'` and `applicationId 'com.aistudio.biothanaweya.xmptqr'` ✅
- `capacitor.config.ts:4` — `appId: 'com.aistudio.biothanaweya.xmptqr'` ✅
- `app/build.gradle.kts:18` — `applicationId = "com.aistudio.biothanaweya.xmptqr"` ✅
- `android/app/src/main/assets/capacitor.config.json:2` — `"appId": "com.aistudio.biothanaweya.exam"` ❌ **Conflict**
- `android/app/src/main/assets/aistudio/Paste_November_11__2026_-_5_38PM_241c4297:273` — `"com.aistudio.biothanaweya.exam"` ❌ **Conflict**

The Debug APK from GitHub Actions (run 44836465341) is built with `com.aistudio.biothanaweya.xmptqr` and verified installed with that identity.

## Decision
**Set the single canonical applicationId to `com.aistudio.biothanaweya.xmptqr`** across all configuration files. Actions:
1. **`capacitor.config.ts`** — already correct: `appId: 'com.aistudio.biothanaweya.xmptqr'` (no change needed).
2. **`android/app/build.gradle`** — already correct: `namespace` and `applicationId` both `com.aistudio.biothanaweya.xmptqr` (no change needed).
3. **`app/build.gradle.kts`** — already correct: `applicationId = "com.aistudio.biothanaweya.xmptqr"` (no change needed).
4. **`android/app/src/main/assets/capacitor.config.json`** — **MUST BE UPDATED**: change `"appId": "com.aistudio.biothanaweya.exam"` to `"appId": "com.aistudio.biothanaweya.xmptqr"` so the asset-level config matches the Gradle/TS configs.
5. **`android/app/src/main/assets/aistudio/Paste_November_11__2026_-_5_38PM_241c4297`** — update line 273 reference from `"com.aistudio.biothanaweya.exam"` to `"com.aistudio.biothanaweya.xmptqr"` (or remove the stale paste reference).

## Consequences
- **Positive**: Single applicationId across all configs; eliminates the `com.aistudio.biothanaweya.exam` vs `com.aistudio.biothanaweya.xmptqr` conflict; Debug APK identity is unambiguous; `grep -rn "applicationId"` will return exactly one value.
- **Negative**: Requires editing two asset files (`capacitor.config.json` and the paste file); if not done, the conflict persists and may affect released APKs.
- **Alternative rejected**: Leaving the conflict in place — would mean the effective applicationId depends on which `capacitor.config.*` the build process picks up, risking mismatched installs, signature errors on upgrade, and Play Store rejection.

## Alternatives Rejected
1. **Keep both applicationIds** — violates the gate requirement that `grep -rn "applicationId" android/ capacitor.config.ts` returns one value only; creates ambiguity for end users and for upgrade compatibility.
2. **Switch to `com.aistudio.biothanaweya.exam`** — the Debug APK and source packages are built for `com.aistudio.biothanaweya.xmptqr`; changing would require re-building the APK, updating source package declarations, and risking breakage of the verified CI pipeline.
3. **Remove `capacitor.config.json` asset** — the asset may be included in the APK by Capacitor; removing it without replacement could cause the runtime to fall back to an default or stale config, producing unpredictable applicationId values.

---
*Decision: Unify applicationId to `com.aistudio.biothanaweya.xmptqr` across all configuration files, starting with the asset-level `capacitor.config.json`.*