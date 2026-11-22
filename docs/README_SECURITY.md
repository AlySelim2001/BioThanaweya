# 🔐 دليل الأمان وحوكمة أسرار توقيع أندرويد (Security & Keystore Governance)

## Android Keystore Generation, Base64 Encoding & GitHub Secrets Setup

وثيقة أمنية شاملة توضح بدقة خطوات توليد مفتاح التوقيع الرقمي (`JKS Keystore`)، وتحويله إلى صيغة Base64، وحقنه داخل أسرار مستودع GitHub Secrets لحماية المفاتيح وتأمين خط الأنابيب الآلي (`.github/workflows/android-release.yml`).

---

### 🛡️ 1. المبادئ الأمنية الصارمة (Zero-Trust Security Principles)

1. **عدم رفع المفاتيح المجردة:** يُحظر تماماً رفع أي ملف يحمل الامتداد `.jks` أو `.keystore` أو `.properties` إلى مستودع Git. ملف `.gitignore` في المشروع مهيأ بالفعل لحجب هذه الامتدادات.
2. **عزل كلمات المرور:** يتم حقن كلمات مرور المخزن والمفتاح عبر المتغيرات البيئية المؤقتة أثناء دورة حياة البناء في GitHub Actions فقط، دون طباعتها في سجلات التشغيل (Log Masking).
3. **الحذف الفوري للملفات المؤقتة:** أي ملف مؤقت يتم استخدامه محلياً لتوليد أو تشفير المفتاح يجب حذفه فوراً بعد نسخه إلى GitHub Secrets.

---

### 🔑 2. خطوات توليد مفتاح التوقيع الرقمي (Generate JKS Keystore)

نفّذ الأمر التالي في سطر الأوامر (Terminal على Linux / macOS / Android Termux / WSL):

```bash
keytool -genkeypair -v \
  -keystore android/app/keystores/biothanaweya-release-key.jks \
  -alias "biothanaweya-alias" \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass "bio_secure_2026_pass" \
  -keypass "bio_secure_2026_pass" \
  -dname "CN=BioThanaweya, OU=Education, O=BioThanaweya, L=Cairo, ST=Cairo, C=EG"
```

> 💡 **ملاحظة:** يمكنك تخصيص كلمات المرور `storepass` و `keypass` بما يناسب معاييرك الأمنية الخاصة، مع الاحتفاظ بالقيم لإدخالها في الخطوات التالية.

---

### 📦 3. تحويل ملف المفتاح إلى نص مشفر بصيغة Base64

لتخزين المفتاح الثنائي (Binary File) كسر نصي آمن داخل GitHub Secrets، قم بتشفيره:

#### على أنظمة Linux أو Android Termux:

```bash
base64 -w 0 android/app/keystores/biothanaweya-release-key.jks > keystore_base64.txt
cat keystore_base64.txt
```

#### على أنظمة macOS:

```bash
base64 -i android/app/keystores/biothanaweya-release-key.jks -o keystore_base64.txt
cat keystore_base64.txt
```

#### على أنظمة Windows (PowerShell):

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("android/app/keystores/biothanaweya-release-key.jks")) | Out-File -Encoding ASCII keystore_base64.txt
Get-Content keystore_base64.txt
```

انسخ النص المشفر الكامل الناتج. فور الانتهاء، قم بحذف الملف المؤقت:

```bash
rm keystore_base64.txt
```

---

### ⚙️ 4. تهيئة أسرار المستودع على GitHub (Configure Repository Secrets)

1. افتح مستودع المشروع على موقع **GitHub**.
2. انتقل إلى التبويب: **Settings** ➔ **Secrets and variables** ➔ **Actions**.
3. اضغط على زر **New repository secret** وأدخل الأسرار الأربعة التالية بدقة:

| اسم السر (Secret Name) | الوصف الهندسي                                   | القيمة الافتراضية / الموصى بها |
| ---------------------- | ----------------------------------------------- | ------------------------------ |
| `KEYSTORE_BASE64`      | النص المشفر الكامل لملف المفتاح بصيغة Base64    | النص المنسوخ من الخطوة 3       |
| `KEYSTORE_PASSWORD`    | كلمة سر مخزن المفاتيح (`storePassword`)         | `bio_secure_2026_pass`         |
| `KEY_ALIAS`            | الاسم المستعار للمفتاح داخل المخزن (`keyAlias`) | `biothanaweya-alias`           |
| `KEY_PASSWORD`         | كلمة سر المفتاح المستعار (`keyPassword`)        | `bio_secure_2026_pass`         |

---

### 🚀 5. التحقق وتشغيل خط الأنابيب الآلي (Execution & Verification)

بمجرد ضبط الأسرار أعلاه، يمكنك إطلاق مسار بناء حزمة الأندرويد الموقعة بعدة طرق:

#### الطريقة الأولى: إنشاء ودفع وسم إصدار جديد (Recommended Git Tag Release)

```bash
git tag -a v1.0.0 -m "BioThanaweya v1.0.0 Production Release"
git push origin v1.0.0
```

#### الطريقة الثانية: التشغيل اليدوي من واجهة GitHub (Manual Workflow Dispatch)

1. من صفحة المستودع على GitHub، اضغط على تبويب **Actions**.
2. اختر مسار العمل: **Android Production Build & Release**.
3. اضغط على القائمة المنسدلة **Run workflow**، واكتب رقم الإصدار (مثل `v1.0.0`) ثم اضغط **Run workflow**.

---

### 📊 6. مخرجات خط الأنابيب والتحقق من التوقيع (Verified Output Artifacts)

1. فحص الجودة المسبق: يمرر `npm run lint` و `npm test` بنسبة 100%.
2. فحص الأمان: تدقيق تلقائي لمنع أي تسريب للمفاتيح في الشفرة المصدرية.
3. التجميع: تجميع الأصول وتحويلها لحزمة أندرويد موقعة في المسار:
   `android/app/build/outputs/apk/release/app-release.apk`
4. النشر: نشر تلقائي في قسم **Releases** مع إرفاق ملف الـ APK المرمز وحساب البصمة الرقمية **SHA-256 Checksum** للتحقق من سلامة الملف قبل التثبيت.
