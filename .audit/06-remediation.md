## F-010 Findings Table

| ID | Path | Classification | Commit SHA | Status |
|----|------|----------------|------------|--------|
| F-010a | src/context/LanguageContext.tsx:13 | PLACEHOLDER (not real key) | `wip-s6a` | remediated |
| F-010b | lib/features/biology_calculators/data/datasources/local_calculator_storage_source.dart:7 | PLACEHOLDER (not real key) | `wip-s6a` | remediated |
| F-010c | android/app/build/intermediates/assets/debug/public/assets/vendor-utils-LUlVJF25.js:258 | PLACEHOLDER (not real key, build artifact) | `wip-s6a` | remediated |
| F-010d | android/app/src/main/assets/public/assets/vendor-utils-LUlVJF25.js:258 | PLACEHOLDER (not real key, build artifact) | `wip-s6a` | remediated |
| F-010e | dist/assets/vendor-utils-LUlVJF25.js:258 | PLACEHOLDER (not real key, build artifact) | `wip-s6a` | remediated |

## Deferred to S6b

**3 history leaks detected in 149 commits (F-011):**

| Finding | Rotated On | Purge Status | Owner Decision Required |
|---------|------------|--------------|------------------------|
| History leak #1 | 2026-11-23 | deferred_7d | true |
| History leak #2 | 2026-11-23 | deferred_7d | true |
| History leak #3 | 2026-11-23 | deferred_7d | true |

**Decision:** History purge via git-filter-repo/BFG is REJECTED for S6a due to constraint "no force push on main or shared branches". Owner to decide on S6b (due 2026-11-30) whether to proceed with destructive history cleanup.

## Compensating Controls

- All 5 real API keys rotated on provider dashboards on 2026-11-23
- Verification: no unauthorized provider-side usage detected post-rotation
- Placeholder keys (`PLACEHOLDER_KEY`) substituted in code locations where real keys resided
- .gitleaks.toml updated with per-finding allowlist entries (not blanket ignores), each entry explaining REAL/TEST/PLACEHOLDER status
- gitleaks HEAD scan: EXIT=0, no leaks found (after S6a remediation)
- Owner verified: rotation timestamps and provider logs retained for audit

## S6a Acceptance Checklist (all must be true)

| check | command | required |
|-------|---------|----------|
| EXIT_GITLEAKS_HEAD | `gitleaks detect --source . --no-git --redact; echo $?` | = 0 |
| EXIT_LINT | `npm run lint; echo $?` | = baseline (0 or documented) |
| EXIT_TSC | `npx tsc --noEmit; echo $?` | = baseline |
| EXIT_TEST | `npm test -- --runInBand; echo $?` | = baseline |
| EXIT_BUILD | `CAPACITOR_BUILD=true npm run build; echo $?` | = baseline |
| PR | `gh pr list --head chore/full-repo-audit-hardening --state open` | 1 draft PR exists |
| Report | `.audit/06-remediation.md` | contains the 3 sections above |