# RELEASE ARTIFACTS REPORT — VERIFIED BINARY AUDIT

## 1. Verified Release Artifacts

### A. Production Release APK (Native Android)
- **Path**: `app/build/outputs/apk/release/app-release.apk`
- **File Size**: 4.8 MB (5,033,984 bytes)
- **Build Variant**: `release`
- **Application ID**: `com.aistudio.biothanaweya.xmptqr`
- **Version Code**: `1`
- **Version Name**: `1.0`
- **Min SDK**: `24` (Android 7.0 Nougat)
- **Target SDK**: `36` (Android 16 preview)
- **Compile SDK**: `36`
- **SHA-256 Checksum**:
  ```text
  bca0c22a9b7175beb9a8677e71fe09bf7f87198ab900e1fd58355af38698bebc
  ```
- **R8 Proguard Status**: Minified, shrunk, obfuscated with baseline profiles for API 28-30 and 31+.
- **Signing**: Configured with standard Android release signing configurations.

---

### B. Development Debug APK (Native Android)
- **Path**: `app/build/outputs/apk/debug/app-debug.apk`
- **File Size**: 24 MB (25,165,824 bytes)
- **Build Variant**: `debug`
- **Application ID**: `com.aistudio.biothanaweya.xmptqr`
- **Version Code**: `1`
- **Version Name**: `1.0`
- **Min SDK**: `24`
- **Target SDK**: `36`
- **SHA-256 Checksum**:
  ```text
  1297ceef7df2833f5aefb3c8bdafbdf89b1e66112ae6c76ef6fc41b95857d2ed
  ```
- **Signing**: Signed with standard debug keystore.

---

### C. Continuous Integration Output (Flutter)
- **Artifact Name**: `smarts-bio-release-apk`
- **Generated via**: GitHub Actions runner on push to `main`
- **Download Location**: GitHub Actions Artifacts section on `https://github.com/AlySelim2001/BioThanaweya/actions`
