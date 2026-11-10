# خطة المعمارية والتنفيذ: وحدة Git & Dev Cheat-Sheet وسير عمل GitHub Actions لـ BioThanaweya

بصفتي مهندس معمارية الهواتف وتطبيقات Flutter/Dart، سنقوم بتنفيذ المتطلبات البرمجية والمعمارية وفق أعلى معايير الـ Clean Architecture وتصميم Bento Grid الحديث، مع تجهيز مسار عمل آلي كامل للنشر وبناء تطبيقات Flutter Android APK.

---

### 1. المكونات والمعمارية المقترحة

#### 📦 أولاً: مسار البناء والنشر الآلي (.github/workflows/deploy.yml)
- **الأحداث المشغلة (Triggers)**:
  - تشغيل تلقائي عند فتح Pull Request على `main` أو فروع `release/*`.
  - تشغيل عند الدفع (Push) أو دمج PR في `main`، أو دفع وسوم الإصدار `v*`.
  - دعم التشغيل اليدوي (`workflow_dispatch`).
- **المراحل والمهام (Jobs)**:
  1. `flutter-preflight-and-test`:
     - فحص الأكواد وتثبيت التبعيات (`flutter pub get`).
     - التحقق الصارم من الأنماط (`flutter analyze`).
     - تنفيذ اختبارات الوحدة والتكامل (`flutter test`).
  2. `flutter-android-build`:
     - بناء حزم APK مقسمة للمعالجات (`flutter build apk --split-per-abi --release`) لتوفير حزم خفيفة الحجم لـ `arm64-v8a` و `armeabi-v7a` و `x86_64`.
     - بناء حزمة `app-bundle` (AAB) لمتجر Google Play.
     - توليد بصمات `SHA256SUMS.txt` وحفظ الملفات كـ Actions Artifacts لمدة 30 يوماً.
  3. `github-release-publish`:
     - نشر تلقائي إلى GitHub Releases عند الاندماج في `main` أو وجود وسم `v*` باستخدام `softprops/action-gh-release@v2`.

#### 📱 ثانياً: وحدة Flutter Git & Dev Cheat-Sheet (`lib/features/git_dev_tools/`)
اتباع مبادئ الـ **Clean Architecture**:
1. **طبقة النطاق والبيانات (Domain & Data Layer)**:
   - `lib/features/git_dev_tools/domain/entities/git_command.dart`:
     - كائن `GitCommand` و `GitCategory` مع حقول: المعرّف، الأمر الفعلي، الشرح بالإنجليزية، الشرح بالعربية، رايات الأوامر، مستوى الخطورة، والحفظ في المفضلة.
   - `lib/features/git_dev_tools/data/datasources/git_commands_database.dart`:
     - مستودع بيانات محلي معبأ بـ 130+ أمراً من المجالات الثمانية (Tracking Changes, Branching, Remote Collaboration, Undo/Recovery, Advanced Branching, Stashing, Debugging/Inspection, Power Commands) ومراحل خارطة الطريق الـ 7.
   - `lib/features/git_dev_tools/data/datasources/local_git_storage_source.dart`:
     - محرك حفظ المفضلة دون إنترنت باستخدام `shared_preferences` مع استرجاع وحفظ فوري.
2. **طبقة العرض والتحكم (Presentation & Riverpod State Layer)**:
   - `lib/features/git_dev_tools/presentation/controllers/git_cheat_sheet_controller.dart`:
     - إدارة حالة البحث، التصنيف المحدد، والمفضلة عبر `StateNotifier` أو `ChangeNotifier` مدعوم بـ Riverpod.
   - `lib/features/git_dev_tools/presentation/screens/git_cheat_sheet_screen.dart`:
     - شاشة Bento Grid متجاوبة تدعم الوضع المظلم والفاتح، والاتجاه العربي RTL والإنجليزي LTR.
     - شريط بحث ذكي، أزرار تصنيف، زر نسخ الأمر إلى الحافظة مع إشعار فوري (Snackbar)، وزر تبديل المفضلة.

---

### 2. خطة الملفات البرمجية المطلوب إنشاؤها

1. `.github/workflows/deploy.yml`: مسار الـ CI/CD الشامل لـ Flutter Android.
2. `lib/features/git_dev_tools/domain/entities/git_command.dart`: نماذج البيانات.
3. `lib/features/git_dev_tools/data/datasources/git_commands_database.dart`: بنك الأوامر الشامل.
4. `lib/features/git_dev_tools/data/datasources/local_git_storage_source.dart`: خدمة التخزين المحلي.
5. `lib/features/git_dev_tools/presentation/controllers/git_cheat_sheet_controller.dart`: المتحكم.
6. `lib/features/git_dev_tools/presentation/screens/git_cheat_sheet_screen.dart`: واجهة Bento Grid.
7. `test/git_cheat_sheet_test.dart`: اختبارات الوحدة للبيانات والتخزين.

---

### 3. خطة التحقق والجاهزية
- كتابة كود Dart و YAML كامل وخالٍ تماماً من الاختصارات أو تعليقات `// TODO`.
- فحص سلامة استدعاءات TypeScript و React في الحاوية لضمان عدم حدوث أي تعارض مع تطبيق الويب الحالي.
