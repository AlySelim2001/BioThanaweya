# 🔐 دليل إعداد أسرار توقيع أندرويد على GitHub
## GitHub Secrets Setup Guide for Android Automated Signing

يقدم هذا الدليل الميداني الخطوات التفصيلية لإعداد أسرار المستودع (`Repository Secrets`) على GitHub لتشغيل خط أنابيب البناء الآلي (`.github/workflows/build-android-release.yml`) وتوقيع حزم أندرويد الإنتاجية (`app-release.apk`) بصورة مؤتمتة ومحمية بالكامل.

---

### 📋 قائمة الأسرار المطلوبة (Required Secrets Matrix)

| اسم السر (Secret Name) | الوصف الهندسي (Description) | مثال على القيمة (Example Value) |
|---|---|---|
| `KEYSTORE_BASE64` | محتوى ملف مفتاح التوقيع (`.jks`) بعد تحويله إلى تشفير Base64 | `MIIKpgIBAzCCCm4GCSqGSIb3DQEHAaCCCm8EggprMIIKazCC...` |
| `KEYSTORE_PASSWORD` | كلمة مرور ملف مخزن المفاتيح (`storePassword`) | `bio_secure_2026_pass` |
| `KEY_ALIAS` | الاسم المستعار للمفتاح داخل المخزن (`keyAlias`) | `biothanaweya-alias` |
| `KEY_PASSWORD` | كلمة مرور المفتاح المستعار نفسه (`keyPassword`) | `bio_secure_2026_pass` |

---

### 🛠️ الخطوة 1: استخراج مفتاح التوقيع بصيغة Base64

إذا كان لديك بالفعل ملف مفتاح التوقيع `biothanaweya-release-key.jks`:

#### على أنظمة Linux / Termux:
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

> **ملاحظة أمنية بالغة الأهمية:** احذف الملف المؤقت `keystore_base64.txt` فور نسخه ولا تقم برفعه أبداً إلى المستودع (`rm keystore_base64.txt`).

---

### 🔑 الخطوة 2: توليد مفتاح جديد (في حال عدم وجود مفتاح سابق)

إذا لم تكن قد أنشأت مفتاح التوقيع بعد، يمكنك توليده في سطر أوامر واحد:
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

ثم قم بتشفيره كما في الخطوة 1.

---

### 🌐 الخطوة 3: إضافة الأسرار إلى مستودع GitHub

1. ادخل إلى صفحة مستودع المشروع على **GitHub**.
2. اضغط على التبويب العلوي **Settings** (الإعدادات).
3. من القائمة الجانبية اليسرى، افتح قسم **Secrets and variables** ثم اختر **Actions**.
4. اضغط على الزر الأخضر **New repository secret**.
5. قم بإضافة الأسرار الأربعة على النحو التالي:
   - **السر الأول:**
     - **Name:** `KEYSTORE_BASE64`
     - **Secret:** الصق النص الطويل الناتج عن تشفير الـ Base64 (دون فواصل أو أسطر إضافية).
     - اضغط **Add secret**.
   - **السر الثاني:**
     - **Name:** `KEYSTORE_PASSWORD`
     - **Secret:** `bio_secure_2026_pass` (أو كلمة المرور التي اخترتها).
     - اضغط **Add secret**.
   - **السر الثالث:**
     - **Name:** `KEY_ALIAS`
     - **Secret:** `biothanaweya-alias`
     - اضغط **Add secret**.
   - **السر الرابع:**
     - **Name:** `KEY_PASSWORD`
     - **Secret:** `bio_secure_2026_pass` (أو كلمة مرور الـ alias).
     - اضغط **Add secret**.

---

### 🚀 الخطوة 4: تشغيل خط أنابيب البناء الآلي (Execution & Verification)

بمجرد ضبط الأسرار، يعمل خط الأنابيب بطريقتين:

#### الطريقة أ: عبر دفع وسم إصدار جديد (Recommended)
```bash
git tag -a v1.0.0 -m "Release v1.0.0: Initial Signed Production APK"
git push origin v1.0.0
```

#### الطريقة ب: يدوياً عبر GitHub Actions Web UI
1. افتح تبويب **Actions** في مستودع GitHub.
2. من القائمة الجانبية، اختر مسار العمل:
   `Build & Publish Android Release APK`
3. اضغط على القائمة المنسدلة **Run workflow**.
4. اختر الفرع (عادة `main`) واكتب رقم الإصدار الاختياري (مثل `v1.0.0`).
5. اضغط **Run workflow**.

---

### 📦 النتيجة المتوقعة (Pipeline Artifacts)
1. تنفيذ فحص الجودة (Linting + Vitest Units + Zero Plaintext Secrets).
2. استخراج وفك تشفير الـ Keystore وتجميع أندرويد عبر Gradle.
3. إنتاج الحزمة الموقعة رقمياً: `android/app/build/outputs/apk/release/app-release.apk`.
4. إنشاء **GitHub Release** تلقائي وإرفاق الحزمة `app-release.apk` مع تجزئة **SHA-256 Checksum** الموثقة.
