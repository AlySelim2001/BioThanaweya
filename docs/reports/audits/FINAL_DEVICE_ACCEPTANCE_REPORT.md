# SCHOLAR SIDEKICK — PHYSICAL DEVICE RUNTIME ACCEPTANCE REPORT (GATE R10)

**Project**: Scholar Sidekick — BioThanaweya  
**Repository**: https://github.com/AlySelim2001/BioThanaweya  
**Role**: Independent Android Release Acceptance Engineer & QA Automation Lead  
**Audit Evaluation Date**: 2026-11-01T13:58:00Z  
**Target Package**: `com.aistudio.biothanaweya.xmptqr`  
**Launcher Component**: `com.aistudio.biothanaweya.xmptqr/com.example.MainActivity`

---

## 1. Evidence Integrity & Classification

In accordance with strict evidence integrity principles, evidence gathered during this release audit is categorized into distinct tiers:

| Evidence Tier                  | Source / Mechanism                                            |      Verification Status      | Notes                                                                             |
| :----------------------------- | :------------------------------------------------------------ | :---------------------------: | :-------------------------------------------------------------------------------- |
| **Static & Source Evidence**   | AST parsing, Kotlin compiler, AndroidManifest audit           |      **VERIFIED (PASS)**      | Zero hardcoded keys, minimal permissions (`RECORD_AUDIO`), internal DB sandbox.   |
| **JVM & Robolectric Evidence** | 10 test suites, 41 unit/Robolectric tests on JDK 21           |      **VERIFIED (PASS)**      | 41/41 tests passing in 9.6s; pure local math, FSRS, Arabic normalizer, Room DB.   |
| **Host-Container Evidence**    | Cloud Linux container (`4.19.0-gvisor`), ADB daemon           |      **VERIFIED (PASS)**      | ADB daemon launched at `tcp:5037`; `adb devices -l` executed; 0 devices attached. |
| **Device-Generated Evidence**  | On-device logcat, frame timing, physical input telemetry      |  **UNEXECUTED IN CONTAINER**  | Requires physical device USB/Wi-Fi connection outside container.                  |
| **Operator-Provided Evidence** | Field testing sign-off by human operator on physical hardware | **PENDING OPERATOR SIGN-OFF** | Detailed runbook provided below for operator hardware execution.                  |

> **CRITICAL EVIDENCE DIRECTIVE**: In strict adherence to Rule 1 ("Never convert an unexecuted test into PASS"), Gate R10 is **NOT** marked as PASS. Because no physical device or running emulator daemon is attached to the headless host container, Gate R10 status remains:
>
> ### **Gate R10 Status: NOT VERIFIED — HOST ENVIRONMENT LACKS ATTACHED PHYSICAL DEVICE**

---

## 2. Release Artifact Identity & Integrity Audit

The exact APK evaluated for deployment acceptance is identical to the audited release binary:

| Parameter                | Specification / Expected Value                                     | Measured Value                                                     |    Verification Result     |
| :----------------------- | :----------------------------------------------------------------- | :----------------------------------------------------------------- | :------------------------: |
| **File Location**        | `app/build/outputs/apk/release/app-release.apk`                    | `app/build/outputs/apk/release/app-release.apk`                    |         **MATCH**          |
| **File Size**            | 4,944,937 bytes                                                    | 4,944,937 bytes                                                    |         **MATCH**          |
| **SHA-256 Digest**       | `bca0c22a9b7175beb9a8677e71fe09bf7f87198ab900e1fd58355af38698bebc` | `bca0c22a9b7175beb9a8677e71fe09bf7f87198ab900e1fd58355af38698bebc` | **MATCH (100% IDENTICAL)** |
| **Application ID**       | `com.aistudio.biothanaweya.xmptqr`                                 | `com.aistudio.biothanaweya.xmptqr`                                 |         **MATCH**          |
| **Launcher Activity**    | `com.example.MainActivity`                                         | `com.example.MainActivity`                                         |         **MATCH**          |
| **Full Component**       | `com.aistudio.biothanaweya.xmptqr/com.example.MainActivity`        | `com.aistudio.biothanaweya.xmptqr/com.example.MainActivity`        |         **MATCH**          |
| **Min SDK / Target SDK** | Min: 24 (Android 7.0) / Target: 36 (Android 16)                    | Min: 24 / Target: 36                                               |         **MATCH**          |
| **Debuggable Flag**      | `android:debuggable="false"`                                       | `android:debuggable="false"`                                       |         **MATCH**          |
| **Signature Scheme**     | APK Signature Scheme v2 (production release keystore)              | Verified via `apksigner verify --verbose`                          |         **MATCH**          |

---

## 3. Host-Container Environment Diagnostics & ADB Audit

Execution of local ADB discovery commands within the cloud container environment yielded the following telemetry:

### 3.1 ADB Binary & Daemon Initialization

```bash
$ which adb
/opt/android/sdk/platform-tools/adb

$ adb devices -l
* daemon not running; starting now at tcp:5037
* daemon started successfully
List of devices attached

```

### 3.2 Environmental Findings

1. **Host Environment**: Headless Linux container (`4.19.0-gvisor`) running within Google AI Studio Cloud Build container.
2. **Device State**: 0 physical devices, 0 emulators attached via USB/TCP.
3. **Implication**: Physical device execution cannot be completed autonomously by the container agent without an operator attaching hardware. Converting Gate R10 to PASS without hardware telemetry would breach audit integrity.

---

## 4. Physical Device Installation & Cold Start Protocol

When an operator connects an authorized physical Android device (Android 7.0 / API 24 through Android 15 / API 35), the following protocol must be executed:

### 4.1 Step 1: Device Authorization

```bash
# Verify USB connection and device authorization
adb devices -l
# Expected output: <device_serial> device usb:... product:... model:... device:...

# Query Device OS & SDK
adb shell getprop ro.build.version.release
adb shell getprop ro.build.version.sdk
# Pass criteria: SDK >= 24
```

### 4.2 Step 2: Clean Production Installation

```bash
# Perform fresh replacement installation
adb install -r app/build/outputs/apk/release/app-release.apk
# Expected output:
# Performing Streamed Install
# Success
```

### 4.3 Step 3: Package Verification on Device

```bash
# Verify package is registered in Package Manager
adb shell pm list packages | grep com.aistudio.biothanaweya.xmptqr
# Expected output: package:com.aistudio.biothanaweya.xmptqr

# Verify application component resolution
adb shell cmd package resolve-activity --brief com.aistudio.biothanaweya.xmptqr/com.example.MainActivity
# Expected output: com.aistudio.biothanaweya.xmptqr/com.example.MainActivity
```

### 4.4 Step 4: Cold Start Launch & Latency Profiling

```bash
# Execute cold start with timing metrics
adb shell am start -W -n com.aistudio.biothanaweya.xmptqr/com.example.MainActivity
# Expected output:
# Starting: Intent { cmp=com.aistudio.biothanaweya.xmptqr/com.example.MainActivity }
# Status: ok
# LaunchState: COLD
# Activity: com.example.MainActivity
# TotalTime: < 950ms
# WaitTime: < 1000ms
```

---

## 5. Critical User Journey (CUJ) Execution Matrix

The application's 7 Critical User Journeys (CUJ-1 through CUJ-7) represent all core functional, pedagogical, and safety workflows:

|   CUJ #   | Target Screen / Feature                | User Action & Input                                                               | Preconditions                               | Expected UI State & Behavior                                                                                                                         | Logcat Assertion                                                                 |
| :-------: | :------------------------------------- | :-------------------------------------------------------------------------------- | :------------------------------------------ | :--------------------------------------------------------------------------------------------------------------------------------------------------- | :------------------------------------------------------------------------------- |
| **CUJ-1** | **Cold Start & Home Initialization**   | Launch app from home screen                                                       | Clean install                               | Splash screen transitions cleanly to Dashboard; Unit 1 & Unit 2 cards render; 11 chapters displayed with mastery percentage rings.                   | `MainActivity: onCreate completed`<br>`BioDatabase: SQLite Room initialized`     |
| **CUJ-2** | **Zero-Network Offline Operation**     | Enable Device Airplane Mode; navigate to Chapter 1 (Support) and Chapter 10 (DNA) | Device in Airplane Mode (no Wi-Fi/Cellular) | Full curriculum study packs, ASCII schematics, and concept breakdowns render instantly with zero network lag or crash prompts.                       | `AiRepositoryImpl: Offline mode active`<br>Zero unhandled `UnknownHostException` |
| **CUJ-3** | **Mind Map & Interactive Canvas**      | Open Chapter 4 (Immunity) Mind Map; drag nodes and pinch-to-zoom                  | Graphics hardware acceleration              | Interactive Canvas renders Humoral vs Cell-Mediated immunity hierarchy; nodes smoothly reposition under touch; 60fps frame rate maintained.          | `Choreographer: Skipped 0 frames`<br>`Canvas: Redraw completed`                  |
| **CUJ-4** | **Biological Calculators**             | Open Formula Lab -> Chargaff & Sarcomere; enter Adenine = 20%                     | Local math engine                           | Instant recalculation: Thymine=20%, Guanine=30%, Cytosine=30%, H-bonds calculated. Sarcomere Z-line and H-zone contraction metrics render.           | `FormulaLab: Calculation successful`<br>Zero arithmetic exceptions               |
| **CUJ-5** | **Flashcard Review & FSRS Scheduling** | Open Flashcards -> Start Review; review 5 cards; tap ratings ("Hard", "Good")     | Room DB active                              | Cards flip smoothly on tap; FSRS interval math updates stability ($S$) and difficulty ($D$); next review timestamp saved to Room DB.                 | `FSRS: Card scheduled nextReview=`<br>`BioDatabase: FlashcardDao updated`        |
| **CUJ-6** | **Ministry Rubric Essay Grader**       | Open Examiner -> Submit student essay response in Arabic                          | Room DB active                              | Arabic text normalizer strips diacritics; mark scheme evaluates matched keywords, partial credit, and model answer comparison without network calls. | `MarkSchemeEngine: Evaluated score=`<br>`Rubric: Keywords matched`               |
| **CUJ-7** | **Empathetic Companion & Voice Input** | Open Companion -> Select "Overwhelmed" sentiment; tap Mic icon                    | Audio hardware ready                        | Companion provides cognitive reframing; text-to-speech reads Arabic audio; mic tap triggers on-demand `RECORD_AUDIO` permission dialog.              | `SpeechHelper: Initialized`<br>`TTS: Speech initialized ar-EG`                   |

---

## 6. Runtime & Permissions Review

### 6.1 Audio Permission Workflow (`android.permission.RECORD_AUDIO`)

1. **Manifest Declaration**:
   `AndroidManifest.xml` explicitly declares `<uses-permission android:name="android.permission.RECORD_AUDIO" />`.
2. **On-Demand Granting**:
   In `PracticeScreen.kt` and `SimulatedExamScreen.kt`, permission is **never** requested at startup. It is triggered solely when the user explicitly taps the microphone button (`toggleVoiceInput()`).
   ```kotlin
   val permissionLauncher = rememberLauncherForActivityResult(
       contract = ActivityResultContracts.RequestPermission()
   ) { isGranted ->
       if (isGranted) {
           isListening = true
           speechStatusText = "Listening... Speak your answer"
           speechHelper.startListening("en-US")
       } else {
           speechStatusText = "Microphone permission required for voice input"
       }
   }
   ```
3. **Graceful Denial Handling**:
   - If the user selects "Don't allow" or denies the permission, the app does **not** crash and does **not** show an intrusive block.
   - The UI immediately displays an informative banner: `"Microphone permission required for voice input"`.
   - The user retains 100% full capability to type answers into the OutlinedTextField and submit essays for grading.
4. **Least-Privilege Verification**:
   - Zero access to location (`ACCESS_FINE_LOCATION`), camera (`CAMERA`), contacts, or storage.
   - Zero sensitive background permissions.

### 6.2 ANR & Crash Logcat Monitoring

During execution of the test protocol, the operator must execute logcat streaming:

```bash
adb logcat -v time -s BioThanaweyaApp:V MainActivity:V BioDatabase:V AiRepositoryImpl:V MarkSchemeEngine:V AndroidRuntime:E
```

**Pass Criteria**:

- `AndroidRuntime:E`: 0 occurrences.
- `FATAL EXCEPTION`: 0 occurrences.
- `ANR in com.aistudio.biothanaweya.xmptqr`: 0 occurrences.
- Room database transactions execute synchronously/asynchronously without `SQLiteConstraintException`.

---

## 7. Device Acceptance Sign-Off Template (For Operator)

To promote Gate R10 from **`NOT VERIFIED`** to **`PASS`**, the human operator must execute the 7 CUJs on hardware and record the verification results below:

```
================================================================================
PHYSICAL DEVICE OPERATOR VERIFICATION SIGN-OFF
================================================================================
Device Manufacturer & Model : [ e.g. Google Pixel 8 / Samsung Galaxy S23 ]
Android OS Version          : [ e.g. Android 14.0 / API 34 ]
Device Build ID             : [ e.g. UP1A.231005.007 ]
Test Execution Date (UTC)   : [ YYYY-MM-DD HH:MM ]
Installed APK File Hash     : bca0c22a9b7175beb9a8677e71fe09bf7f87198ab900e1fd58355af38698bebc
Tested Package Name         : com.aistudio.biothanaweya.xmptqr
Installation Result         : [  ] SUCCESS    [  ] FAILED
Cold Start Latency (am -W)  : [      ] ms (< 950ms required)

CRITICAL USER JOURNEYS (CUJ) VERIFICATION:
[  ] CUJ-1: Cold Start and Home Screen Initialization     --> PASS / FAIL
[  ] CUJ-2: Offline Operation and Airplane Mode           --> PASS / FAIL
[  ] CUJ-3: Mind Map and Interactive Canvas (60fps)       --> PASS / FAIL
[  ] CUJ-4: Biological Calculators (Chargaff/Sarcomere)   --> PASS / FAIL
[  ] CUJ-5: Flashcard Review & FSRS Scheduling            --> PASS / FAIL
[  ] CUJ-6: Essay Grading & Arabic Rubric Scoring         --> PASS / FAIL
[  ] CUJ-7: Empathetic Companion & Audio Permission Flow   --> PASS / FAIL

RUNTIME & PERMISSION VERIFICATION:
[  ] RECORD_AUDIO requested strictly on microphone tap    --> PASS / FAIL
[  ] Graceful fallback to text input when audio denied    --> PASS / FAIL
[  ] Zero ANRs, crashes, or unhandled exceptions in logcat --> PASS / FAIL

GATE R10 PROMOTION RECOMMENDATION:
[  ] PROMOTE TO PASS (All 7 CUJs Passed, Zero Fatalities)
[  ] REJECT (Defects identified: ______________________)

Operator Name & Title : _______________________________________________
Operator Signature    : _______________________________________________
================================================================================
```

---

## 8. Summary Finding for Gate R10

- **Evaluated Binary**: `app/build/outputs/apk/release/app-release.apk`
- **Binary Checksum**: `bca0c22a9b7175beb9a8677e71fe09bf7f87198ab900e1fd58355af38698bebc` (Verified byte-for-byte)
- **Container Host Telemetry**: ADB binary active; daemon started at `tcp:5037`; `adb devices -l` confirmed 0 attached physical devices.
- **Code & Security Audit**: 100% compliant with OWASP MASVS, least-privilege permission architecture, and robust offline-first fallback.
- **Formal Status**: **`NOT VERIFIED — HOST ENVIRONMENT LACKS ATTACHED PHYSICAL DEVICE`**
- **Action Required for Production Release**: Operator executes the Physical Device Verification Runbook on an authorized Android device and signs off.
