# 🧬 Scholar Sidekick (BioThanaweya / smarts.bio)

> **The Next-Generation Offline-First Learning Ecosystem for Egyptian Thanawya Amma Biology**

[![Build Status](https://github.com/AlySelim2001/BioThanaweya/actions/workflows/build.yaml/badge.svg)](https://github.com/AlySelim2001/BioThanaweya/actions)
[![Platform Support](https://img.shields.io/badge/Platform-Android%20%7C%20Cross--Platform-blue.svg)](https://www.android.com)
[![Architecture](https://img.shields.io/badge/Architecture-Offline--First%20Bento-success.svg)](#-core-architectural-pillars)
[![Flutter Version](https://img.shields.io/badge/Flutter-3.19.x%20%7C%20Stable-02569B?logo=flutter)](https://flutter.dev)
[![JDK](https://img.shields.io/badge/JDK-17%20Temurin-ED8B00?logo=openjdk)](https://adoptium.net)
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
* **The 3D-Like Cellular Organelle Viewport (`ModernCellSimulationScreen`):** Interactive visual simulation of mitochondria, nucleus, chloroplasts, and ribosomes with official ministerial exam takeaways.

---

## 📚 Curriculum Mapping (11 Thanawya Amma Chapters)

| # | Chapter Title (بالعربية) | English Topic | Focus Areas |
|---|---|---|---|
| **01** | الدعامة والحركة في الكائنات الحية | Support & Movement | Physiological/structural support, sliding filament theory |
| **02** | التنسيق الهرموني في الكائنات الحية | Hormonal Coordination | Endocrine glands, feedback loops, thyroxine, ADH |
| **03** | التكاثر في الكائنات الحية | Reproduction | Parthenogenesis, alternation of generations, gametogenesis |
| **04** | المناعة في الكائنات الحية | Immunity | Innate vs. adaptive immunity, perforin, humoral & cell-mediated |
| **05** | DNA والمحمل الوراثي وتضاعفه | DNA & Replication | Double helix, helicase, DNA polymerase, ligase, Okazaki |
| **06** | الأحماض النووية RNA وبناء البروتين | RNA & Protein Synthesis | Transcription, AUG translation, ribosomes, genetic code |
| **07** | التقنية الحيوية وهندسة الوراثة | Biotechnology | Bacterial restriction enzymes, PCR, recombinant DNA |
| **08** | التغذية والهضم والتمثيل الغذائي | Nutrition & Digestion | Photosynthesis, Calvin cycle, PGAL, digestive enzymes |
| **09** | النقل والتنفس الخلوي وإنتاج الطاقة | Transport & Respiration | Cohesion-tension, transpiration, glycolysis, Krebs, 38 ATP |
| **10** | الإخراج والتنظيم الأسموزي والحراري | Excretion | Nephron ultrafiltration, selective reabsorption, skin |
| **11** | المراجعات الشاملة والمحاكي الوزاري الفائق | Comprehensive Exam Simulator | Timed full-length ministerial mock exams and trap analysis |

---

## 🚀 Getting Started & Local Development

### Prerequisites
* Flutter SDK (v3.19.x or higher)
* Dart SDK
* Java JDK 17 (Eclipse Temurin)
* Android SDK / Command Line Tools (minSdk 24, compileSdk 36)

### Installation Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/AlySelim2001/BioThanaweya.git
   ```

2. Navigate to the project directory:
   ```bash
   cd BioThanaweya
   ```

3. Install dependencies:
   ```bash
   flutter pub get
   ```

4. Run the application:
   ```bash
   flutter run
   ```

5. Run local tests:
   ```bash
   flutter test
   ```

---

## ⚙️ Automated CI/CD & Releases
The project utilizes GitHub Actions to automate release builds defined in [`.github/workflows/build.yaml`](.github/workflows/build.yaml). Every verified push to `main` triggers a clean build sequence that:
1. Dynamically detects the project workspace root.
2. Provisions Java 17 and Flutter stable SDK.
3. Resolves dependencies and compiles release APK binaries via `flutter build apk --release`.
4. Calculates SHA-256 integrity checksums.
5. Publishes `smarts-bio-release-apk` as a downloadable artifact retained for 30 days.

---

## 📞 Contact & Support
* **Developer / Creator:** علي سليم (Aly Selim)
* **WhatsApp:** [+201119886662](https://wa.me/201119886662)
* **GitHub Repository:** [https://github.com/AlySelim2001/BioThanaweya](https://github.com/AlySelim2001/BioThanaweya)

---

## 📄 License
Distributed under the **MIT License**. See [LICENSE](LICENSE) for more information.
