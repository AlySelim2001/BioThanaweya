# 🤝 Contributing to BioThanaweya

Thank you for your interest in contributing to **BioThanaweya**!  
BioThanaweya is an open-source, non-profit, 100% FOSS educational platform built to support Egyptian Thanaweya Amma (Languages Schools) biology students.

---

## 📜 Core Principles & Ground Rules

1. **100% Free & Open Source (FOSS)**:
   - No proprietary software, tracking beacons, telemetry, advertisements, or paywalled dependencies.
   - All client features must operate fully offline via PWA and local IndexedDB.
2. **Official Ministry Syllabus Alignment**:
   - Every scientific explanation, formula, and diagram must strictly align with the Egyptian Ministry of Education Biology Curriculum (Units 1 & 2 for Languages Schools).
   - Mark schemes and grading criteria must enforce deductions for omitted structural landmarks and enzymes ($Ca^{2+}$, $ACh$, cholinesterase, ligase, etc.).
3. **Low-Resource Mobile First**:
   - The codebase must run smoothly on low-spec Android devices and Termux container environments with memory limits under 128MB RAM.
4. **Clean Code & Strict Typing**:
   - Written exclusively in strict TypeScript (`strict: true`, zero `any` unless explicitly guarded).
   - Clean Architecture separation between UI screens, presentation components, domain services, and storage engines.

---

## 🛠️ Local Development Setup

### Standard Node.js Environment (Linux, macOS, Windows)

```bash
# 1. Clone repository
git clone https://github.com/AlySelim2001/BioThanaweya.git
cd BioThanaweya

# 2. Install dependencies using npm
npm install

# 3. Start full-stack development server (Express + Vite)
npm run dev

# 4. Or start client-only Vite server
npm run dev:client
```

The app will be accessible at `http://localhost:3000`.

### Termux Environment (Android)

```bash
# 1. Update and install packages
pkg update -y && pkg install nodejs-lts git -y

# 2. Clone and navigate
git clone https://github.com/AlySelim2001/BioThanaweya.git
cd BioThanaweya

# 3. Install dependencies
npm install

# 4. Launch in Termux
npm run dev
```

---

## 🌿 Git Branching & Workflow

1. Fork or branch from `main`:
   ```bash
   git checkout -b feat/your-feature-name
   # or
   git checkout -b fix/issue-description
   ```
2. Commit your changes using **Conventional Commits**:
   - `feat:` Adds a new user-facing capability or biological formula solver.
   - `fix:` Fixes a calculation error, layout bug, or broken link.
   - `docs:` Updates documentation or curriculum explanation notes.
   - `refactor:` Code refactoring that neither fixes a bug nor adds a feature.
   - `perf:` Performance optimization or memory footprint reduction.

Example:

```bash
git commit -m "feat(calculators): add Okazaki fragment count helper for lagging strand"
```

---

## 🧪 Pre-Flight Quality Verification

Before opening a Pull Request, always verify that your changes pass all linters and build cleanly without regressions:

```bash
# 1. Verify strict TypeScript compliance and linting
npm run lint

# 2. Verify production bundle and PWA service worker compilation
npm run build

# 3. Preview built production bundle locally
npm run preview
```

## 🛠️ CI/CD Verification (GitHub Actions)

All PRs are automatically validated by the repository's CI/CD pipeline. Before merging, ensure:

- **Typecheck**: `npm run lint` passes (zero TypeScript errors)
- **Unit tests**: `npm test` passes (all 72+ tests)
- **Security scan**: No secrets or vulnerable dependencies detected
- **Build**: Production web build (`npm run build`) succeeds
- **Android**: If modifying native assets, run `bash scripts/sync-android-assets.sh`

The CI pipeline will also:
- Generate SBOM (Software Bill of Materials) from `package-lock.json`
- Run `npm audit --audit-level=high` for dependency vulnerabilities
- Scan for secret credentials in source code
- Verify artifact integrity with SHA-256 checksums

**Do not push PRs that fail any CI check.** Fix failures locally before re-opening the PR.

---

## 📬 Pull Request Checklist

- [ ] Strict TypeScript checks pass with zero warnings (`npm run lint`).
- [ ] Production build succeeds (`npm run build`).
- [ ] No hardcoded API keys, secrets, or sensitive tokens.
- [ ] Arabic terminology and English scientific concepts align with official Ministry textbooks.
- [ ] UI elements maintain responsive mobile ergonomics ($\ge 44\text{px}$ touch targets).

Thank you for helping empower Egyptian students! 🇪🇬🧬
