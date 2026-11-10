# خطة المعمارية والتنفيذ: المعمارية النظيفة ومحرك قواعد البيانات المحلي لـ BioThanaweya

بصفتي مهندس معمارية الهواتف وتطبيقات Flutter/Dart، سنقوم بتنفيذ المخطط المعماري الشامل (Clean Architecture Blueprint) ومحرك قواعد البيانات المحلي ومكون لوحة المعمارية (Architecture Hub Widget) وفق المتطلبات المحددة وبدون أي تبعيات خارجية غير متوافقة.

---

### 1. شجرة مجلدات المعمارية النظيفة (Clean Architecture Directory Structure)

```text
lib/
├── core/                               # النواة المشتركة لجميع وحدات التطبيق
│   ├── architecture/                   # لوحة ومخطط المعمارية الشاملة
│   │   └── app_architecture_hub.dart   # شاشة العرض التفاعلية لأركان التطبيق
│   ├── constants/                      # الثوابت وبيانات المنهج الوزاري
│   │   └── curriculum_data.dart
│   ├── database/                       # محرك التخزين المحلي SQL-Compatible
│   │   ├── local_database_service.dart # خدمة تهيئة الجداول والاستعلامات
│   │   └── models/                     # نماذج الجداول (فصول، أسئلة، كتب)
│   ├── network/                        # خدمات الاتصال والأمان
│   │   └── gemini_service.dart
│   ├── services/                       # خدمات البيانات والتخزين
│   │   ├── bio_app_controller.dart
│   │   └── curriculum_data_service.dart
│   └── theme/                          # سمات التطبيق (Dark/Light & RTL)
│       └── app_theme.dart
│
├── features/                           # وحدات الميزات المقسمة معمارياً (Feature-First)
│   ├── achievements/                   # شارات الإنجاز ونقاط XP
│   ├── biology_agents/                 # وكلاء الذكاء الاصطناعي الـ 7
│   ├── biology_lab/                    # معمل الرسوم البيانية التفاعلية
│   ├── developer_resources/            # مركز موارد المطورين ومستودعات GitHub
│   ├── exam_simulator/                 # شاشة محاكاة امتحان الوزارة
│   ├── flashcards/                     # بطاقات الاسترجاع المتباعد SM-2/FSRS
│   ├── git_dev_tools/                  # دليل أوامر Git التفاعلي
│   ├── interactive_suite/              # المنظومة التعليمية التفاعلية
│   ├── smart_quiz_engine/              # محرك الاختبارات الذكي
│   └── study_planner/                  # المخطط الدراسي وجدول المذاكرة
│
└── main.dart                           # نقطة الانطلاق الرئيسية للتطبيق
```

---

### 2. محرك التخزين المحلي (`lib/core/database/local_database_service.dart`)
- **المعمارية**: محرك SQL-Compatible متكامل مكتوب بلغة Dart الصافية (Pure Dart) يعمل بذاكرة فورية وفهرسة سريعة مدعومة بـ `SharedPreferences` للتخزين الدائم دون الاعتماد على مكتبات C-Native SQLite المعقدة.
- **الجداول الأساسية**:
  - `chapters_table`: فصول المنهج الوزاري المصري (الوحدتان الأولى والثانية).
  - `textbooks_table`: كتب الوزارة وكتيب المفاهيم المعتمد مع مسارات التحميل والأوفلاين.
  - `offline_exam_cache_table`: أسئلة الامتحانات الرسمية ومحاولات الطلاب.
- **العمليات المدعومة**:
  - `createTablesIfNotExists()`: بناء هياكل الجداول.
  - `seedDefaultCurriculumData()`: ملء بيانات المنهج فوراً عند أول تشغيل.
  - `queryChapters()`, `queryTextbooks()`, `saveExamAttempt()`.

---

### 3. شاشة لوحة المعمارية (`lib/core/architecture/app_architecture_hub.dart`)
- تصميم Bento Grid تفاعلي يبرز أركان بناء التطبيق:
  1. **UI/UX Paradigm**: توضيح معمارية Flutter و Hot Reload والتصميم المتجاوب لـ RTL.
  2. **Local Database & Offline State**: حالة محرك البيانات المحلي والجاهزية للعمل دون إنترنت.
  3. **Multi-Platform Native Container**: تشغيل التطبيق كـ Android Native و PWA.
  4. **GitHub CI/CD Deployment**: حالة مسارات البناء المؤتمتة وتوليد الـ APK.
- إمكانية طي وفرد الأقسام، واختبار استعلام قاعدة البيانات الحية، واختبار مسار العمل.

---

### 4. الاختبارات والتحقق
- كتابة اختبارات وحدة شاملة في `test/local_database_service_test.dart`.
- فحص سلامة جميع الأكواد وخلوها تماماً من أي تعليقات اختصار.
