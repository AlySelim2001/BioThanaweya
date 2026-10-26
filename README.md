# 🧬 BioThanaweya (أحياء الثانوية العامة)
> منصة تعليمية ذكية متقدمة لطلاب الثانوية العامة في جمهورية مصر العربية، تعتمد على معمارية Clean Architecture ونظام التكرار المتباعد الفائق (FSRS) ومحاكاة لجان الامتحانات الرسمية.

---

## 📑 جدول المحتويات (Table of Contents)
1. [نظرة عامة على المشروع (Overview)](#-نظرة-عامة-على-المشروع)
2. [المعمارية وهيكل الحزم (Clean Architecture)](#-المعمارية-وهيكل-الحزم)
3. [التقنيات والمكتبات المستخدمة (Tech Stack)](#-التقنيات-والمكتبات-المستخدمة)
4. [متطلبات التشغيل (Prerequisites)](#-متطلبات-التشغيل)
5. [خطوات فتح المشروع في Android Studio](#-خطوات-فتح-المشروع-في-android-studio)
6. [إعداد وربط Firebase](#-إعداد-وربط-firebase)
7. [تشغيل التطبيق والمحاكي (Running the Emulator)](#-تشغيل-التطبيق-والمحاكي)
8. [الاختبارات وبناء الـ Release](#-الاختبارات-وبناء-الـ-release)

---

## 🎯 نظرة عامة على المشروع
تطبيق **BioThanaweya** مصمم خصيصاً لمساعدة طلاب الثانوية العامة في مصر على إتقان منهج الأحياء من خلال:
- **نظام التكرار المتباعد FSRS**: خوارزمية ذكية (Free Spaced Repetition Scheduler) لحساب فترات مراجعة بطاقات الاستذكار بناءً على ثبات الذاكرة وصعوبة المفهوم.
- **محاكي الامتحانات الوزارية**: تجربة مطابقة للامتحان النهائي مع حساب درجات الأسئلة وتحديد فخاخ التفكير والتفسيرات النموذجية.
- **معمل القوانين والمسائل**: تدريب على مسائل الوراثة، حساب جزيئات ATP، وقطع الـ DNA.
- **تفسيرات متعددة المستويات**: شرح مبسط (ELI5)، تحليل تقني عميق، ومخططات توضيحية بصيغة Monospace دقيقة.

---

## 🏗️ المعمارية وهيكل الحزم
تم بناء التطبيق باتباع معمارية **Clean Architecture** الصارمة مع فصل المسؤوليات:

```text
com.example/
├── data/                           # طبقة البيانات (Data Layer)
│   ├── local/                      # التخزين المحلي (Room Database)
│   │   ├── dao/                    # واجهات الوصول للبيانات (DAOs)
│   │   │   ├── FlashcardDao.kt
│   │   │   ├── ProgressDao.kt
│   │   │   ├── ExamAttemptDao.kt
│   │   │   └── TopicMasteryDao.kt
│   │   ├── entity/                 # جداول قاعدة البيانات (Room Entities)
│   │   │   ├── FlashcardEntity.kt
│   │   │   ├── StudentProgressEntity.kt
│   │   │   ├── ExamAttemptEntity.kt
│   │   │   └── TopicMasteryEntity.kt
│   │   └── BioDatabase.kt          # قاعدة بيانات التطبيق المحلية
│   ├── model/                      # كائنات تحويل البيانات (Data Models / Mappers)
│   └── repository/                 # تنفيذ المستودعات (Repository Implementations)
│       ├── BiologyRepositoryImpl.kt
│       └── AiRepositoryImpl.kt
│
├── domain/                         # طبقة الأعمال والنطاق (Domain Layer)
│   ├── fsrs/                       # خوارزمية التكرار المتباعد FSRS v5/v6
│   │   └── FSRS.kt                 # حسابات Stability, Difficulty, Retrievability
│   ├── model/                      # نماذج الدومين النقية
│   │   └── BiologyModels.kt
│   ├── repository/                 # واجهات المستودعات (Repository Interfaces)
│   │   ├── BiologyRepository.kt
│   │   └── AiRepository.kt
│   └── usecase/                    # حالات الاستخدام والوكلاء التعليميون
│       └── AgentUseCases.kt
│
├── presentation/                   # طبقة واجهة المستخدم (Presentation Layer)
│   ├── components/                 # مكونات Compose القابلة لإعادة الاستخدام
│   │   └── SpecializedComponents.kt
│   ├── screens/                    # شاشات التطبيق الكاملة
│   │   ├── DashboardScreen.kt
│   │   ├── TopicDetailScreen.kt
│   │   ├── PracticeScreen.kt
│   │   ├── FsrsReviewScreen.kt
│   │   ├── SimulatedExamScreen.kt
│   │   ├── FormulaLabScreen.kt
│   │   └── SettingsScreen.kt
│   └── viewmodel/                  # نماذج العرض وإدارة الحالة (ViewModels)
│       ├── BioViewModel.kt
│       └── FsrsReviewViewModel.kt
│
├── di/                             # حقن الاعتماديات (Dependency Injection)
│   ├── AppModule.kt                # توفير كائنات التطبيق و Coroutine Dispatchers
│   ├── DatabaseModule.kt           # توفير قاعدة البيانات والـ DAOs
│   ├── NetworkModule.kt            # توفير OkHttp و Retrofit و Moshi
│   └── AppContainer.kt
│
├── ui/theme/                       # نسق وألوان وتيبوغرافيا Material 3
└── MainActivity.kt                 # نقطة الدخول الرئيسية مع @AndroidEntryPoint
```

---

## 🛠️ التقنيات والمكتبات المستخدمة
تتم إدارة جميع الاعتماديات مركزياً عبر **Gradle Version Catalog** (`gradle/libs.versions.toml`):
- **UI Toolkit:** Jetpack Compose (Compose BOM 2024.09.00) + Material Design 3.
- **Dependency Injection:** Hilt (Dagger) `2.51.1` مع `@HiltAndroidApp` و `@AndroidEntryPoint`.
- **Database:** Room Database `2.7.0` مدعوماً بـ KSP (Kotlin Symbol Processing).
- **Spaced Repetition:** مكتبة `ru.lorddarthart.fsrs:fsrs` مدمجة مع خوارزمية FSRS الرياضية في الدومين.
- **Networking:** Retrofit `2.12.0` + Moshi `1.15.2` + OkHttp `4.10.0`.
- **Firebase:** Firebase BOM `34.17.0` (Firebase AI Logic + App Check).
- **Concurrency:** Kotlin Coroutines & StateFlow.
- **Testing:** JUnit 4 + Robolectric `4.16.1` + Coroutines Test.

---

## 📋 متطلبات التشغيل
- **Android Studio:** إصدار **Hedgehog (2023.1.1)** أو **Iguana** أو **Ladybug (2024.2+)** أو أحدث.
- **JDK:** Java Development Kit إصدار **JDK 17**.
- **Android SDK:**
  - `minSdk`: **24** (Android 7.0 Nougat فما فوق)
  - `compileSdk`: **36**
  - `targetSdk`: **36**

---

## 💻 خطوات فتح المشروع في Android Studio
1. من بيئة **AI Studio**، اضغط على زر **Export** ثم اختر **Download ZIP**.
2. فك ضغط الملف في مجلد على حاسوبك (مثال: `~/StudioProjects/BioThanaweya`).
3. افتح **Android Studio**، واختر **Open**.
4. حدد المجلد الرئيسي للمشروع الذي يحتوي على `settings.gradle.kts` و `build.gradle.kts`.
5. سيبدأ Android Studio تلقائياً في مزامنة الـ Gradle (Sync Project with Gradle Files).
6. تأكد من ضبط الـ JDK في Android Studio على **JDK 17**:
   - `Settings` (أو `Preferences` على Mac) ⬅️ `Build, Execution, Deployment` ⬅️ `Build Tools` ⬅️ `Gradle` ⬅️ اختر `Gradle JDK: 17`.

---

## 🔥 إعداد وربط Firebase
التطبيق مهيأ للعمل بشكل كامل محلياً بدون إيقاف أو أخطاء، ولتفعيل خدمات الذكاء الاصطناعي (Firebase AI Logic) وحماية التطبيق (App Check):
1. انتقل إلى [Firebase Console](https://console.firebase.google.com/).
2. أنشئ مشروعاً جديداً أو استخدم مشروعاً قائماً.
3. أضف تطبيق Android باسم الحزمة (Application ID):
   ```text
   com.aistudio.biothanaweya.xmptqr
   ```
4. قم بتنزيل ملف `google-services.json`.
5. ضع الملف في مسار الموديول:
   ```text
   BioThanaweya/app/google-services.json
   ```
6. أعد مزامنة المشروع في Android Studio.

---

## 📱 تشغيل التطبيق والمحاكي
1. في شريط الأدوات العلوي بـ Android Studio، اختر جهازاً من قائمة **Device Manager** (مثلاً: `Pixel 7 API 34`).
2. إذا لم يكن لديك محاكي مُنشأ مسبقاً:
   - اضغط على **Device Manager** ⬅️ **Create Virtual Device**.
   - اختر **Phone** ⬅️ **Pixel 7** أو **Pixel 8**.
   - اختر صورة النظام **API 34 (Android 14)** أو **API 33**.
   - اضغط **Finish**.
3. اضغط على زر التشغيل الأخضر **Run 'app'** (أو اختصار `Shift + F10`).
4. سيبدأ تشغيل التطبيق على المحاكي مباشرة.

---

## 🧪 الاختبارات وبناء الـ Release
### تشغيل اختبارات الوحدة محلياً:
```bash
./gradlew testDebugUnitTest
```
أو عبر Gradle Task في Android Studio:
`Tasks -> verification -> testDebugUnitTest`

### بناء ملف APK تجريبي (Debug APK):
```bash
./gradlew assembleDebug
```
المسار الناتج: `app/build/outputs/apk/debug/app-debug.apk`

### بناء نسخة الـ Release:
قواعد ProGuard مجهزة ومضبوطة بالكامل في `app/proguard-rules.pro` لحماية الكود ومنع تشويش مكونات Hilt و Firebase و Room.
```bash
./gradlew assembleRelease
```

---

## 🚀 خط الأنابيب الآلي (GitHub Actions CI/CD)
تم إعداد Workflow متكامل في `.github/workflows/build.yml` للقيام بما يلي تلقائياً:
1. البناء التلقائي عند كل `push` إلى فرع `main`.
2. استخراج وفك تشفير الـ Keystore وإعدادات Firebase عبر Secrets مشفرة.
3. تجميع حزمة الإنتاج `assembleRelease` وتطبيق قواعد ProGuard.
4. رفع ملف الـ APK كـ Artifact.
5. عند إنشاء أي Tag يبدأ بـ `v*` (مثال: `v1.0.0`)، يتم إنشاء **GitHub Release** رسمي وإرفاق ملف الـ APK به تلقائياً.

### 🔐 الأسرار المطلوبة (GitHub Repository Secrets):
انتقل إلى `Settings -> Secrets and variables -> Actions` وأضف الأسرار التالية:
* **`RELEASE_KEYSTORE_BASE64`**: ملف الـ Keystore مشفر بـ Base64 (`base64 -w 0 release.jks`).
* **`KEYSTORE_PASSWORD`**: كلمة مرور ملف الـ Keystore.
* **`KEY_PASSWORD`**: كلمة مرور المفتاح الخاص (Key Alias Password).
* **`FIREBASE_CONFIG_BASE64`**: ملف `google-services.json` مشفر بـ Base64 (`base64 -w 0 app/google-services.json`).

