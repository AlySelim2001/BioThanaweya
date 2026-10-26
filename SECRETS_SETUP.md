# 🔐 GitHub Repository Secrets Setup Guide

This guide provides step-by-step instructions for configuring the required GitHub Secrets to enable automated building, signing, and releasing of the **BioThanaweya** Android application via GitHub Actions.

---

## 📋 Required Secrets Overview

Configure the following 5 secrets in your GitHub repository:

| Secret Name | Description | Example / Format |
| :--- | :--- | :--- |
| **`KEYSTORE_BASE64`** | Your Android release signing Keystore file (`release.jks` or `upload.jks`), encoded in Base64 string format. | `MIIT...==` (single line) |
| **`KEYSTORE_PASSWORD`** | The password used to lock/unlock your Keystore file. | `YourKeystorePassword123` |
| **`KEY_ALIAS`** | The alias identifying the private key entry inside the Keystore. | `upload` (or your custom alias) |
| **`KEY_PASSWORD`** | The password protecting the specific private key entry. | `YourKeyPassword123` |
| **`GOOGLE_SERVICES_JSON_BASE64`** | Your Firebase configuration file (`google-services.json`), encoded in Base64 string format. | `eyJ...==` (single line) |

---

## 🛠️ How to Encode Files as Base64

To safely pass binary and sensitive JSON files through GitHub Secrets without corruption or line-break issues, encode them into single-line Base64 strings.

### 1. Keystore File (`release.jks`)

#### On Linux / Ubuntu:
```bash
base64 -w 0 release.jks > keystore_base64.txt
cat keystore_base64.txt
```

#### On macOS:
```bash
base64 -i release.jks -o keystore_base64.txt
cat keystore_base64.txt
```

#### On Windows (PowerShell):
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("release.jks")) | Set-Content keystore_base64.txt
Get-Content keystore_base64.txt
```

---

### 2. Firebase Configuration (`google-services.json`)

#### On Linux / Ubuntu:
```bash
base64 -w 0 app/google-services.json > google_services_base64.txt
cat google_services_base64.txt
```

#### On macOS:
```bash
base64 -i app/google-services.json -o google_services_base64.txt
cat google_services_base64.txt
```

#### On Windows (PowerShell):
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("app/google-services.json")) | Set-Content google_services_base64.txt
Get-Content google_services_base64.txt
```

---

## 🧭 How to Add Secrets to Your GitHub Repository

1. Navigate to your GitHub repository in your web browser.
2. Click on the **Settings** tab at the top of the repository.
3. In the left sidebar, expand **Secrets and variables** and select **Actions**.
4. Click the green **New repository secret** button.
5. Enter the **Name** exactly as specified (e.g., `KEYSTORE_BASE64`).
6. Paste the entire Base64 string (or password) into the **Secret** field.
7. Click **Add secret**.
8. Repeat these steps for all 5 secrets:
   - `KEYSTORE_BASE64`
   - `KEYSTORE_PASSWORD`
   - `KEY_ALIAS`
   - `KEY_PASSWORD`
   - `GOOGLE_SERVICES_JSON_BASE64`

---

## 🧪 Local Verification of Base64 Encoding

You can quickly verify that your Base64 strings decode back into valid files before adding them to GitHub:

```bash
# Test decoding keystore
echo "<PASTE_BASE64_HERE>" | base64 --decode > test_keystore.jks
keytool -list -v -keystore test_keystore.jks

# Test decoding google-services.json
echo "<PASTE_BASE64_HERE>" | base64 --decode > test_google_services.json
cat test_google_services.json | grep "project_id"
```

---

## ⚠️ Security Best Practices

- **Never Commit Raw Keys:** Ensure `*.jks`, `*.keystore`, and `google-services.json` are listed in `.gitignore` and never committed into public version control.
- **Revocation:** If credentials or passwords are leaked, immediately rotate the private key and update the secrets in GitHub.
- **Repository Visibility:** Make sure repository collaborator permissions are restricted to trusted individuals who should have access to run deployment workflows.
