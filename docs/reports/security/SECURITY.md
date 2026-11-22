# 🔒 Security Policy - BioThanaweya

## Supported Versions

| Version | Supported | Security Updates |
| ------- | :-------: | ---------------- |
| 1.1.5   | ✅ Yes     | Critical fixes released |
| 1.1.4   | ✅ Yes     | Critical fixes released |
| < 1.1.4 | ❌ No     | No active support |

## Reporting Vulnerabilities

If you discover a security vulnerability in BioThanaweya, please report it responsibly:

1. **Email**: security@biothanaweya.example (or open a private GitHub Security Advisory)
2. **Do NOT** disclose vulnerabilities in public issues or pull requests until a fix is released
3. **Expected response time**: Within 5 business days for acknowledgment
4. **Fix timeline**: Critical vulnerabilities will be addressed within 30 days

## Security Principles

BioThanaweya is built with the following security commitments:

### 1. Client-Side Privacy First

- All student progress, flashcards, and exam data reside locally on the device
- **No student data is transmitted to external servers** unless explicitly opted-in (exam grading via FreeLLMAPI)
- IndexedDB and localStorage data is never uploaded without user-initiated export
- The application functions fully offline without requiring network connectivity

### 2. API Key Protection

- **FreeLLMAPI/Gemini API keys are NEVER stored in client-side code**
- Keys are managed exclusively in `server.ts` environment variables
- All AI grading requests flow through the server-mediated `/api/examiner/grade` endpoint
- The client sends only `{questionPrompt, studentAnswer, modelAnswer, requiredKeywords, maxScore}`
- Server handles API key rotation, rate limiting, and response validation

### 3. Supply Chain Security

- All dependencies are pinned via `package-lock.json` with deterministic installation
- `npm audit --audit-level=high` is enforced in CI/CD
- Secrets scanning detects API keys, AWS credentials, and private keys in source
- Dependencies with known high/critical vulnerabilities block merges
- Override rules for `glob` and `minimatch` (pinned to secure versions)

### 4. No Hardcoded Secrets

- **.env files with real values must NOT be committed** (only `.env.example` is tracked)
- GitHub Actions secrets are used for any required API keys
- The `.env.example` template shows required variables without values
- Pre-commit hook enforces no-secret-commit policy (recommended)

### 5. Dependency Vetting

- All new dependencies require documented engineering reason (ADR-006)
- Major version upgrades require compatibility evidence
- `npm audit` runs at `audit-level=high` in every CI run
- `dependabot.yml` enables weekly dependency update PRs (max 5 open at a time)
- No unpaid/unknown 3P APIs added without review

### 6. Build Security

- GitHub Actions workflows use least-privilege permissions
- Concurrency controls prevent overlapping runs on same ref
- No privileged execution of untrusted fork code (jobs skip on PRs unless repo owner)
- Build provenance attached to every artifact (commit SHA, branch, timestamp)
- SBOM (Software Bill of Materials) generated from `package-lock.json`

### 7. Data Sensitivity Classification

| Data Category | Classification | Protection |
| ------------- | -------------: | ---------- |
| Student progress (streaks, badges, exam scores) | Medium | IndexedDB local only; no transmission |
| Exam results & grades | Medium | IndexedDB local only; auto-snapshots user-controlled |
| FSRS card state | Medium | IndexedDB local only; no PII |
| API keys (FreeLLMAPI, Gemini) | **High** | Server-only; never client-side |
| Curriculum content | Low | Bundled in app; educational content only |
| JSON backups exported by user | User-controlled | User decides storage; app validates on import |

## Threat Model Mitigations

| Threat | Likelihood | Impact | Mitigation |
| ------ | :--------: | :----: | ---------- |
| **API Key Exposure** | Low-Med | High | Server-mediated AI calls only; keys in `server.ts` env vars |
| **Client-side Data Corruption** | Low | High | Type guards on all IndexedDB reads; legacy migration validation |
| **IndexedDB Quota Exceeded** | Medium | Medium | Quota metrics displayed; read-only mode if full |
| **localStorage Leakage** | Low | Medium | Validation on every migration pass; only valid structures accepted |
| **Prompt Injection via AI** | Low | Medium | All retrieved content validated before rendering; student answers sanitized |
| **WebView Boundary Violation** | Medium | High | Capacitor Whitelist configured; HTTPS-only; no `allowNav` to unknown origins |
| **Unsigned APK Distribution** | Medium | Medium | GitHub Actions builds signed debug APKs; release candidates signed with env vars |
| **GitHub Actions Permissions Escalation** | Low | Minimal | Minimal required permissions only (checkout, setup-node) |
| **Malicious Package Dependency** | Low | High | `npm audit --audit-level=high`; dependencies pinned via lockfile |

## Security Checklist for Contributors

Before committing any code, verify:

- [ ] `npm run lint` passes (zero TypeScript errors)
- [ ] `npm test` passes (all unit tests)
- [ ] No new API keys, secrets, or credentials added to source
- [ ] No `AIza`, `ghp_`, or `BEGIN RSA PRIVATE KEY` patterns in changed files
- [ ] `npm audit --audit-level=high` shows no high/critical vulnerabilities
- [ ] Production build (`npm run build`) succeeds
- [ ] Capacitor assets synced if modifying native resources (`bash scripts/sync-android-assets.sh`)
- [ ] CHANGELOG.md updated with change description
- [ ] Type definitions updated if adding new public APIs
- [ ] Arabic terminology and English scientific concepts aligned with Ministry textbooks

## Security Scanning in CI

The GitHub Actions pipeline automatically runs these security checks on every push and PR:

1. **Secret scanning** - Detects `AIza`, `ghp_`, `BEGIN RSA PRIVATE KEY` patterns
2. **Dependency security** - `npm audit --audit-level=high`
3. **License compliance** - Verifies MIT license presence
4. **SBOM generation** - Open-source cyclonedx from package-lock.json
5. **Build provenance** - Commit SHA, branch, timestamp metadata attached to artifacts

## Past Security Incidents

No security incidents have been reported for BioThanaweya. The type-safe IndexedDB validation (TASK-002) and legacy migration layer were specifically added to prevent data corruption from IndexedDB manipulation or corrupted localStorage backups.

## Contact

For security questions or vulnerability reports, please contact the maintainers via GitHub issues or the developer contact listed in the repository.