# [BIOTHANAWEYA ENTERPRISE MASTER UNIFIED EXECUTION & REFACTORING DIRECTIVE]

You are an elite Principal Full-Stack, Mobile & DevOps Architect. Your mission is to execute an exhaustive, uncompromising architectural overhaul, code cleanup, security hardening, advanced feature implementation, and production-grade mobile compilation for **BioThanaweya** (a React, Vite, TypeScript, Vitest, PWA, and Capacitor-based high school biology education platform).

You must strictly adhere to the architectural governance, modular boundaries, and rules defined in the repository's `AGENT_SKILLS.md` file.

Execute the following 5 phases systematically, rigorously, and without skipping any steps:

---

### PHASE 1: Deep Workspace Sanitation & AGENT_SKILLS Governance
1. **Purge Build Artifacts & Junk Files:** Recursively find and remove all accidental build outputs, compiled assets, or nested caches that shouldn't be tracked (e.g., `dist/`, `build/`, `android/app/src/main/assets/public/`, `node_modules/`, `.cache/`, `.DS_Store`, `Thumbs.db`, `repomix-output.*`).
2. **Enforce `.gitignore` & Git Rigor:** Ensure `.gitignore` explicitly blocks all build artifacts, environment files (`.env*`), and mobile wrapper output folders. Run `git rm --cached` on any accidentally tracked directories matching ignore rules. Ensure `.gitattributes` forces `text=auto eol=lf`.
3. **Modular Architecture Compliance:** Verify and enforce strict feature-driven isolation (`src/features/exams/`, `src/features/simulations/`, `src/features/memory/`, and `src/services/`). Prevent circular dependencies completely.

---

### PHASE 2: Comprehensive Codebase & Dependency Audit
1. **Strict TypeScript Pass (Zero-Error Policy):** Run `npx tsc --noEmit` to scan the entire codebase. Enforce the zero-`any` policy. Fix all type mismatches, missing exports, implicit `any` types, or broken interfaces to guarantee **0 errors or warnings**.
2. **Dependency & Vulnerability Remediation:** Audit `package.json` and lockfiles for peer dependency conflicts and security vulnerabilities. Resolve any version mismatches cleanly.
3. **Dead Code & Lifecycle Management:** Scan for unused variables, dead imports, or unhandled promise rejections. Ensure all `useEffect` hooks clean up listeners and timers properly to prevent memory leaks in resource-constrained environments (Termux/Mobile).

---

### PHASE 3: Advanced Pedagogical & Simulation Features Implementation
1. **FSRS Algorithm & Exam Engine:** Implement and integrate the Free Spaced Repetition Scheduler (`src/services/`) and verify precise Egyptian Thanaweya Amma exam scoring logic (`MinistryExamScoringEngine`).
2. **Interactive DNA Simulation Suite (`src/features/simulations/dna/`):** 
   - `dna.types.ts`: Define strict types for nitrogenous bases (`A, T, C, G, U`) and enzyme states.
   - `useDnaReplication.ts`: Build the hook managing hydrogen bond breaking and complementary base pairing (`5' to 3'`).
   - `DnaStrandVisualizer.tsx`: Create responsive SVG and Framer Motion animated visualizations wrapped in Error Boundaries.

---

### PHASE 4: Automated Testing Suite & Verification (Vitest)
1. **Test Execution & Coverage:** Run the complete test suite (`npx vitest run`) covering curriculum exams, FSRS memory algorithms, and the DNA simulation suite (`dna.test.ts`).
2. **Bug Fixing & Quality Gate:** If any test fails, trace the root cause, fix the logic or mocks, and re-run until **100% of tests pass successfully**.

---

### PHASE 5: Production Build & Native Android APK Pipeline
1. **Optimized Web Production Bundle:** Execute `npm run build` to verify zero bundling errors, proper PWA service worker generation (Workbox), and optimized Vite manual chunk splitting (`vendor-react`, `vendor-core`, `vendor-motion`).
2. **Capacitor Mobile Sync:** Run `npx cap sync android` to sync web assets into the Android native module (`android/app/src/main/assets/public/`).
3. **Native APK Compilation:** Navigate to the `android/` directory, grant execution rights to Gradle wrapper (`chmod +x gradlew`), and execute `./gradlew assembleDebug`.
4. **Final Verification & Report:** Output the exact path of the generated standalone mobile binary (`android/app/build/outputs/apk/debug/app-debug.apk`) and provide a detailed summary of all modifications, code hygiene actions, and performance gains achieved.

