# 📖 Repository Documentation Index

This directory contains the authoritative documentation hierarchy for the **BioThanaweya** project.

## Documentation Structure

| Category | Path | Description |
|----------|------|-------------|
| **Architectural Decision Records** | `docs/architecture/` | ADRs documenting key design decisions and trade-offs |
| **Development Guide** | `docs/development/` | Developer setup, contribution guidelines, and implementation details |
| **Releases** | `docs/releases/` | Release acceptance matrices and release-related reports |
| **Architecture Reports** | `docs/reports/architecture/` | High-level architecture descriptions and analyses |
| **Audits** | `docs/reports/audits/` | Audit reports (AI agents, verification, security, device acceptance) |
| **Build Reports** | `docs/reports/builds/` | CI/CD validation, APK integrity, and build verification reports |
| **Security** | `docs/reports/security/` | Security audit reports and setup guides |
| **Testing** | `docs/reports/testing/` | Test execution and quality assurance reports |
| **Historical** | `docs/reports/historical/` | Preserved historical reports that document prior findings and states |

## Key Documentation Files

- **[AI Agents Audit](reports/audits/AI_AGENTS_AUDIT.md)** — 5-agent system validation
- **[AI Agents Acceptance Report](reports/audits/FINAL_AI_AGENTS_ACCEPTANCE_REPORT.md)** — R6 & R7 gate verification
- **[Architecture ADRs](architecture/)** — Application ID unification, Android delivery, Flutter archival
- **[Developer Guide](development/DEVELOPER_GUIDE.md)** — Field guide and contribution conventions
- **[Security Setup](security/README_SECURITY.md)** — Android signing secrets configuration
- **[Release Artifacts](releases/FINAL_RELEASE_ACCEPTANCE_MATRIX.md)** — Android APK release pipeline documentation
- **[Changelog](../CHANGELOG.md)** — Versioned change history

## Historical Reports Index

This repository preserves historical reports in `docs/reports/historical/` to maintain a complete audit trail. These reports document the state and conclusions recorded at their original dates and may not describe the current implementation. Do not assume current applicability.

- **[Master Audit Report](historical/MASTER_AUDIT_REPORT.md)** — Comprehensive initial audit
- **[Engineering Execution Roadmap](historical/ENGINEERING_EXECUTION_ROADMAP.md)** — Phase-plan execution track
- **[Freebuff Release Investigation](historical/FREEBUFF_V1.1.8_RELEASE_STATE_INVESTIGATION.md)** — v1.1.8 state investigation
- **[Performance Report](builds/PERFORMANCE_REPORT.md)** — Performance profiling results
- **[Production Readiness](builds/PRODUCTION_READINESS_REPORT.md)** — Release readiness assessment
- And others...

## Linking Conventions

- Use relative links starting with the project root: `../CHANGELOG.md` for root-level files
- For files within `docs/`, use: `docs/README.md` or `docs/architecture/AI_AGENTS.md`
- Root-level files reference `README.md` directly

---

*Last updated: Documentation reorganization complete. For the current project entry point, see the root [README.md](../README.md).*