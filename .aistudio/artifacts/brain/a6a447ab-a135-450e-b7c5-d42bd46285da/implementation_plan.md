# خطة المعمارية والتنفيذ: وحدة مركز موارد المطورين (Developer Resource Hub)

بصفتي مهندس معمارية الهواتف وتطبيقات Flutter/Dart، سنقوم بتنفيذ وحدة **Developer Resource Hub** لـ BioThanaweya تحت مسار `lib/features/developer_resources/` وفق أصول الـ Clean Architecture وشبكة Bento Grid الحديثة، لدعم استكشاف أفضل 25 مستودعاً على GitHub لتطوير المهارات دون الحاجة لأي خدمات سحابية مدفوعة.

---

### 1. المعمارية المعتمدة والتصنيفات
- **مسار الوحدة**: `lib/features/developer_resources/`
- **التصنيفات الأربعة الأساسية**:
  1. **Learn & Roadmaps (التعلم والخرائط)**: `developer-roadmap`, `freeCodeCamp`, `coding-interview-university`, `computer-science-curriculum`.
  2. **Build & Projects (المشاريع والتطبيق)**: `build-your-own-x`, `app-ideas`, `project-based-learning`, `free-programming-books`.
  3. **System Design & Clean Code (المعمارية والكود النظيف)**: `system-design-primer`, `clean-code-javascript`, `design-patterns-for-humans`, `awesome-architecture`.
  4. **APIs & Tools (الواجهات والأدوات)**: `public-apis`, `awesome`, `awesome-flutter`, `developer-cheat-sheets`.

- **آلية فتح الروابط**:
  - نسخ الرابط المباشر إلى الحافظة (`Clipboard`) مع اهتزاز لمسي (`HapticFeedback`) وإشعار فوري (Snackbar).
  - إظهار نافذة تفاعلية (`Quick Overview Dialog`) تعرض تفاصيل المستودع، التراخيص، النجوم، ووسائل الاستفادة منه في مشاريع الطلاب والمطورين.

- **التخزين المحلي (Offline-first)**:
  - استخدام `SharedPreferences` لتخزين معرفات المستودعات المفضلة محلياً في الذاكرة دون الحاجة للإنترنت.

---

### 2. خطة الملفات البرمجية المطلوب إنشاؤها

1. `lib/features/developer_resources/domain/entities/developer_resource_model.dart`:
   - نماذج البيانات `DevResourceItem` و `ResourceCategory` مع `toMap()`, `fromMap()`, و `copyWith`.
2. `lib/features/developer_resources/data/datasources/developer_resources_database.dart`:
   - قاعدة البيانات المحلية المعبأة مسبقاً بأفضل 25 مستودعاً من الإنفوجرافيك بتفاصيل دقيقة ثنائية اللغة.
3. `lib/features/developer_resources/data/datasources/local_resource_storage_source.dart`:
   - خدمة حفظ واسترجاع المستودعات المفضلة باستخدام `shared_preferences`.
4. `lib/features/developer_resources/presentation/controllers/developer_resource_hub_controller.dart`:
   - متحكم الحالة (`ChangeNotifier`) لإدارة البحث الفوري والتصفية والمفضلة.
5. `lib/features/developer_resources/presentation/screens/developer_resource_hub_screen.dart`:
   - شاشة Bento Grid متجاوبة تدعم الوضعين المظلم والفاتح، والاتجاه العربي RTL والإنجليزي LTR.
6. `test/developer_resource_hub_test.dart`:
   - اختبارات وحدة شاملة للتأكد من صحة البيانات وعمليات التصفية والتخزين.

---

### 3. معايير الجودة وخلو الأخطاء
- كود Dart كامل ومكتمل (بدون أي تعليقات اختصار مثل `// TODO`).
- التحقق المتقاطع مع سير العمل والاختبارات للتأكد من خلوه تماماً من أي تعارضات.
