# SECURITY AUDIT REPORT (OWASP MASVS ALIGNED) — SCHOLAR SIDEKICK

## 1. Scope & Standards
This security audit evaluates Scholar Sidekick (`smarts.bio` - BioThanaweya) against the OWASP Mobile Application Security Verification Standard (MASVS) baseline categories:
- MASVS-STORAGE (Storage & Data Privacy)
- MASVS-CRYPTO (Cryptography)
- MASVS-AUTH (Authentication & Authorization)
- MASVS-NETWORK (Network Security)
- MASVS-PLATFORM (Platform Interaction & IPC)
- MASVS-CODE (Code Quality & Build Hardening)

---

## 2. Findings Register

| Category | Control / Area | Evaluation / Evidence | Status |
|----------|----------------|-----------------------|--------|
| **MASVS-STORAGE** | Plaintext PII Leakage | No student sensitive PII (passwords, payment cards, national IDs) is collected or stored. Progress data is strictly academic. | **PASS** |
| **MASVS-STORAGE** | SQLite / Room Protection | Room database stored inside internal app sandbox (`/data/data/com.example/databases/`). External world-readable storage is never used. | **PASS** |
| **MASVS-CRYPTO** | Hardcoded Secrets | Source audit confirms zero hardcoded private API keys or certificates in code. API keys are injected via BuildConfig / Secrets Gradle plugin. | **PASS** |
| **MASVS-NETWORK** | Cleartext HTTP Traffic | AndroidManifest.xml does not set `usesCleartextTraffic="true"`. All remote endpoints use TLS 1.3/HTTPS. | **PASS** |
| **MASVS-PLATFORM** | Component Export (IPC) | `MainActivity` has `android:exported="true"` strictly as the launcher intent-filter target. All internal services/receivers default to `exported="false"`. | **PASS** |
| **MASVS-PLATFORM** | Permissions Minimization | Zero dangerous permissions requested (no CAMERA, RECORD_AUDIO, or broad storage access). Strict least-privilege compliance. | **PASS** |
| **MASVS-CODE** | R8 Minification & Obfuscation | `minifyEnabled = true` and `shrinkResources = true` active in release build, stripping unused classes and reducing binary footprint. | **PASS** |
| **MASVS-CODE** | Keystore & Signing Integrity | Debug keystore is isolated. Release signing configs preserved without destructive tampering. | **PASS** |
