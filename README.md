
# 🧬 Scholar Sidekick (BioThanaweya / smarts.bio)

> **The Next-Generation Offline-First Learning Ecosystem for Egyptian Thanawya Amma Biology**

[![Build Status](https://github.com/AlySelim2001/BioThanaweya/actions/workflows/build.yaml/badge.svg)](https://github.com/AlySelim2001/BioThanaweya/actions)
[![Platform Support](https://img.shields.io/badge/Platform-Android%20%7C%20Cross-Platform-blue)]()
[![Architecture](https://img.shields.io/badge/Architecture-Offline--First%20Bento-success)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---

## 🌟 Overview & Vision
**Scholar Sidekick (BioThanaweya)** is a state-of-the-art mobile educational ecosystem engineered specifically for Egyptian high school science students. Built with an uncompromising commitment to performance, privacy, and zero-latency learning, the application operates **100% offline**, ensuring uninterrupted access to the complete biology curriculum anywhere, anytime.

---

## 🏛️ Core Architectural Pillars

1. **100% Offline-First Engine:** 
   * Zero network dependency for core learning modules.
   * Instant local caching and reactive state management powered by `SharedPreferences` and local JSON asset bundling.
   * Real-time local auto-save for exam simulations, guaranteeing zero data loss.

2. **Comprehensive Curriculum Scope (11 Chapters):**
   * Covers all Egyptian Thanawya Amma biology modules—from *Structural Support & Movement in Living Organisms* and *Hormonal Coordination* to *Reproduction, Immunity, Molecular Genetics (DNA & RNA), and Advanced Biotechnology*.

3. **Cyber-Biology Bento Grid & Glassmorphism UI:**
   * Ultra-modern OLED dark theme palette (`#0A192F` Obsidian background, `#112240` glass cards, `#64FFDA` Neon Teal accents, and `#2E7D32` Emerald indicators).
   * Native Arabic RTL layout (`TextDirection.rtl`, `ar_EG` locale) with precise Cairo typography hierarchy.

4. **Automated DevOps & CI/CD Pipeline:**
   * Fully automated GitHub Actions workflow (`.github/workflows/build.yaml`) that verifies dependencies, compiles Flutter artifacts, and builds production-ready release APKs.

---

## 📱 Core Interface Suite
* **The Mastery Dashboard (`BoundDashboardScreen`):** Real-time progress tracking across all 11 chapters with circular completion rings and quick-launch navigation.
* **The Immersive Exam Simulator (`BoundExamScreen`):** Timed ministerial-style mock exams with instant local state persistence and option feedback.
* **The Offline Full-Text Search Engine (`BoundSearchScreen`):** Sub-millisecond local keyword indexing across questions, explanations, and terms.

---

## 🚀 Getting Started & Local Development

### Prerequisites
* Flutter SDK (v3.19.x or higher)
* Dart SDK
* Android SDK / Command Line Tools

### Installation Steps
1. Clone the repository:
   ```bash
   git clone [https://github.com/AlySelim2001/BioThanaweya.git](https://github.com/AlySelim2001/BioThanaweya.git)

 * Navigate to the project directory:
   cd BioThanaweya

 * Install dependencies:
   flutter pub get

 * Run the application:
   flutter run

⚙️ Automated CI/CD & Releases
The project utilizes GitHub Actions to automate release builds. Every verified push triggers a clean build sequence that compiles the release APK and attaches it directly as a downloadable Artifact in the Actions tab.
📞 Contact & Support
 * Developer / Creator: علي سليم (Aly Selim)
 * WhatsApp: +201119886662
📄 License
Distributed under the MIT License. See LICENSE for more information.
