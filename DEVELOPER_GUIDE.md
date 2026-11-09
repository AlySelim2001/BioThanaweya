# 🛠️ BioThanaweya Developer Field Guide (دليل المطور الميداني)

> **Official Mobile & Low-Resource Engineering Manual**  
> Comprehensive operational field guide for developers, maintainers, and educators building and deploying `BioThanaweya` across Android Termux, low-memory containers, and standard Linux workstations.

---

## 📑 Table of Contents / فهرس المحتويات
1. [📱 Module 1: Termux & Mobile Field Operations (دليل التشغيل الميداني)](#1--module-1-termux--mobile-field-operations)
2. [🏛️ Module 2: Clean Architecture & Domain Layering (الهيكل المعماري)](#2--module-2-clean-architecture--domain-layering)
3. [📊 Module 3: System Data Flow & ASCII Sequence Diagrams (مخططات تدفق البيانات)](#3--module-3-system-data-flow--ascii-sequence-diagrams)
4. [🧯 Module 4: Mobile Troubleshooting Playbook (دليل حل المشكلات الشائعة)](#4--module-4-mobile-troubleshooting-playbook)
5. [🧪 Module 5: Quality Assurance & Release Governance (الجودة والإصدارات)](#5--module-5-quality-assurance--release-governance)

---

## 1. 📱 Module 1: Termux & Mobile Field Operations

BioThanaweya is engineered to run seamlessly inside **Termux** on non-rooted Android devices with resource throttling designed to prevent background OS process termination.

### Clean Setup from Zero (التهيئة النظيفة من الصفر)
Open the Termux terminal and execute the following bootstrap sequence:

```bash
# 1. Update Termux base repository mirrors
pkg update -y && pkg upgrade -y

# 2. Install Node.js v22 LTS, Git, and process utilities
pkg install nodejs-lts git psmisc curl -y

# 3. Verify toolchain versions
node -v    # Must be v22.x or later
npm -v     # Must be v10.x or later
git --version

# 4. Clone the repository
git clone https://github.com/AlySelim2001/BioThanaweya.git
cd BioThanaweya

# 5. Deterministic dependency installation
npm install
```

### Execution Modes Comparison (أوضاع التشغيل المتاحة)

| Command | Environment | Memory Target | Purpose |
| :--- | :--- | :--- | :--- |
| `npm run dev` | Full-Stack (Express + Vite) | $\le 128\text{MB}$ RSS | Production-simulated server with memory circuit breaker and API proxies. |
| `npm run dev:client` | Client-Only Vite | $\le 64\text{MB}$ RSS | Lightweight mobile frontend editing with minimal battery consumption. |
| `npm run verify` | Termux Pre-Flight Check | Capped at 256MB | Runs strict linting, 21 unit tests, and production PWA compilation. |
| `npm run release` | Semantic Versioning Tool | Interactive CLI | Bumps `package.json`, stamps `CHANGELOG.md`, and generates `git tag`. |
| `npm run preview` | Production Web Preview | Static HTTP | Serves the pre-compiled `dist/` PWA bundle on `0.0.0.0:3000`. |

```bash
# Launch default full-stack server
npm run dev

# Open in Android browser: http://localhost:3000
```

---

## 2. 🏛️ Module 2: Clean Architecture & Domain Layering

The codebase strictly adheres to Clean Architecture principles to ensure complete isolation of UI components from persistence and business rules:

```text
src/
├── types/index.ts             # Domain Layer: Pure interfaces (FSRSCard, TopicMastery, WellbeingProfile)
├── services/                  # Application & Infrastructure Layer:
│   ├── db/
│   │   ├── indexedDbEngine.ts # Core Storage: Native IndexedDB engine (BioThanaweyaDB_v1)
│   │   └── backupService.ts   # Serialization: JSON export/import & rolling auto-snapshots
│   ├── storage.ts             # Cache-Aside Orchestrator: In-memory cache + async DB sync
│   ├── calculators.ts         # Pure Mathematical Solvers: Sarcomere, Chargaff, and Codon laws
│   ├── examinerEngine.ts      # Ministry Evaluation: Keyword extraction & mark deduction rules
│   ├── fsrs.ts                # Spaced Repetition: FSRS v4.5/v5 scheduling calculations
│   └── haptics.ts             # Hardware Abstraction: Web Vibration API wrapper
├── components/                # Presentation Layer: Reusable UI widgets & PWA controls
├── screens/                   # View Layer: Full curriculum, exam, and lab screens
└── server/                    # Backend Infrastructure: Express server, memory breaker, routes
```

---

## 3. 📊 Module 3: System Data Flow & ASCII Sequence Diagrams

### Diagram 1: Transactional Persistence & Real-Time Sync Lifecycle
Shows how state changes (e.g. card review or profile update) are updated synchronously in memory for 60 FPS rendering, while being committed asynchronously to IndexedDB:

```
[ User Action: Review Card ]
             │
             ▼
      [ StorageService ]
             │
             ├─────────────────────────────────────────┐
             ▼ (Synchronous Update)                    ▼ (Asynchronous Persistence)
     [ In-Memory Cache ]                     [ persistAsync() Worker ]
             │                                         │
             ▼                                         ├─► notifySyncState('saving')
      [ React State ]                                  │          │
             │                                         │          ▼
   (Instant UI Re-render)                              │   [ Navbar: "Saving..." ]
                                                       │
                                                       ▼
                                            [ IndexedDbEngine ]
                                            (BioThanaweyaDB_v1)
                                                       │
                                                       ├─► db.put('cards', card)
                                                       ├─► AutoSnapshot (Milestone)
                                                       │
                                                       ▼
                                            notifySyncState('synced')
                                                       │
                                                       ▼
                                            [ Navbar: "Local DB Synced" ]
```

---

### Diagram 2: Ministry Examination Evaluation & Mark Scheme Flow
Shows how student answers are graded against official Ministry criteria, enforcing deductions for omitted structural landmarks before updating FSRS spaced repetition stability:

```
[ Student Prompt Answer ] ──► (Voice or Typed Input)
             │
             ▼
  [ examinerEngine.ts ]
             │
             ├─► Step 1: Detect Required Keywords (e.g., "Ca²⁺ ions", "Sarcoplasmic Reticulum")
             ├─► Step 2: Check Critical Trap Omissions (e.g., missing ACh / Cholinesterase)
             │        │
             │        ├─► Keyword Missing? ──► Apply Mandatory Deduction Points
             │        └─► Keyword Present? ──► Award Full / Partial Credit
             │
             ▼
  [ ExamResultReport ] ──► (Score, Letter Grade, Feedback, Remediation Advice)
             │
             ├─────────────────────────────────────────┐
             ▼                                         ▼
   [ SimulatedExamScreen ]                     [ FSRS Engine (fsrs.ts) ]
   (Renders Report Dialog)                             │
             │                                         ├─► Calculate next stability (S)
             ▼                                         ├─► Calculate difficulty (D)
   [ haptics.celebrate() ]                             └─► Calculate next interval
   (Tactile micro-burst)                                       │
                                                               ▼
                                                    [ StorageService.saveCard() ]
                                                               │
                                                               ▼
                                                    [ Rolling Auto-Snapshot ]
```

---

## 4. 🧯 Module 4: Mobile Troubleshooting Playbook

### Issue 1: Termux Out-Of-Memory (OOM) Process Killed
- **Symptoms**: The build or dev server stops abruptly with `Killed` or `Out of memory` logged by Android kernel.
- **Root Cause**: Node.js defaults to 2GB or 4GB heap size, exceeding Android's background cgroup limits (typically 384MB–512MB per app).
- **Remedy**: Constrain the V8 old space size before running Node:
  ```bash
  export NODE_OPTIONS="--max-old-space-size=256"
  npm run dev
  ```
  *Note*: This flag is automatically embedded in `npm run verify`.

---

### Issue 2: Port Collision (`EADDRINUSE 0.0.0.0:3000`)
- **Symptoms**: `Error: listen EADDRINUSE: address already in use 0.0.0.0:3000`.
- **Root Cause**: A previous Node or Vite process remains running in the background after closing Termux.
- **Remedy**: Terminate orphaned processes bound to port 3000:
  ```bash
  # Identify and terminate process using port 3000
  fuser -k 3000/tcp

  # Or kill all hanging node processes
  pkill -f node

  # Re-verify port availability
  curl -I http://localhost:3000
  ```

---

### Issue 3: Corrupted or Stale IndexedDB Quota
- **Symptoms**: Changes fail to persist, or `StorageVaultModal` shows sync error state.
- **Root Cause**: Browser storage sandbox reached quota limits or private browsing mode restricted IndexedDB storage.
- **Remedy**:
  1. Open the app at `http://localhost:3000`.
  2. Click **Local DB** in the top navigation bar to open the **Storage Vault**.
  3. Click **Download JSON Backup** to secure existing data.
  4. Click **Factory Reset & Re-seed** to re-initialize stores.
  5. Click **Restore JSON Vault** to re-import your backup.

---

### Issue 4: Git Remote Synchronization & Detached Tags on Mobile
- **Symptoms**: `git push origin main` fails with rejection or detached HEAD warnings.
- **Remedy**:
  ```bash
  # Ensure you are on the main branch
  git checkout main

  # Fetch latest remote changes and rebase
  git fetch origin
  git rebase origin/main

  # Push commits and release tags together
  git push origin main --tags
  ```

---

## 5. 🧪 Module 5: Quality Assurance & Release Governance

### Running the Vitest Unit Test Suite
The project includes 21 comprehensive unit tests verifying formulas and haptic handlers:

```bash
# Run all unit tests once
npm test

# Run tests in watch mode during development
npm run test:watch
```

### Automated Local Verification Script
Before committing or pushing changes to GitHub, execute:

```bash
npm run verify
# or directly:
bash scripts/verify-local.sh
```
This script executes four automated gates:
1. Enforces the 256MB memory boundary.
2. Runs strict TypeScript type-checking (`tsc --noEmit`).
3. Executes all 21 Vitest unit tests.
4. Compiles production assets and PWA service worker.

### Generating Semantic Releases
To release a new version from Termux:

```bash
npm run release
```
Select the bump type (`patch`, `minor`, `major`). The script automatically:
1. Updates the version in `package.json`.
2. Adds a stamped release header to `CHANGELOG.md`.
3. Creates an annotated Git commit and tag (`vX.Y.Z`).
4. Outputs the exact push command to trigger GitHub Actions release packaging.

---

## 📄 License & Governance

Distributed under the **MIT License**. 100% Free and Open Source Software (FOSS). Built in service of Egyptian Thanaweya Amma students.
