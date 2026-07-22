# Developer Guide & Session History

This document serves as an internal guide for the developer to remember all the steps, commands, and fixes applied during this session. 

**AI Session ID:** `68c6e9f9-d4e9-497f-859a-cb3dfdb7748b`
**Date & Time of Creation:** July 2, 2026

---

## 📱 App Configuration Details
- **App Name:** QR Scanner & Reader [No ADs] (Play Console Name: QR Scanner & Reader)
- **Package Name:** `com.ritikqrscanner.app`
- **Current Version Code:** 2
- **Current Version Name:** 1.0.1
- **Target SDK:** 35 (Android 15+)
- **Min SDK:** 24 (Android 7.0+)
- **Keystore Password:** `ritikqr2026#`

---

## 🛠️ Common Commands

### 1. Build Commands
**Generate Release App Bundle (For Play Console Upload):**
```bash
./gradlew bundleRelease
```
*Output path: `app/build/outputs/bundle/release/app-release.aab`*

**Generate Release APK (For Direct Install/Testing):**
```bash
./gradlew assembleRelease
```
*Output path: `app/build/outputs/apk/release/app-release.apk`*

### 2. Version Change Instructions
To update the app version for a new release, modify the `app/build.gradle.kts` file:
```kotlin
defaultConfig {
    // Increment this by 1 for every upload to Play Console!
    versionCode = 3 
    
    // Change this to reflect feature updates (e.g., 1.0.2 or 1.1)
    versionName = "1.0.2" 
}
```

### 3. GitHub Release Creation (CLI)
To publish a new version directly to GitHub Releases with attached files:
```bash
gh release create v1.0.1 --title "Version 1.0.1" --notes "Release notes here" app/build/outputs/bundle/release/app-release.aab app/build/outputs/apk/release/app-release.apk
```

---

## 🚀 Google Play Console History
**What we accomplished today:**
1. **Keystore Generation:** Created a persistent `release.keystore` and configured `build.gradle.kts` to automatically sign production builds.
2. **Asset Fixing:** 
   - Re-sized the High-Res App Icon to exactly `512x512` pixels (`play_store_icon.png`).
   - Generated a custom `1024x500` Feature Graphic (`feature_graphic.png`).
3. **API Level Compliance:** Play Console rejected the initial API 34 target. We successfully bumped `compileSdk` and `targetSdk` to **35**.
4. **Version Bump:** Uploading the initial bundle consumed `versionCode = 1`. The live testing bundle is now running on `versionCode = 2`.
5. **Closed Testing Track:** Because the Google account is new (post-Nov 2023), the app was deployed to the **Closed Testing Track** (requires 20 testers for 14 continuous days).

**What to do next in Play Console:**
- Finish setting up your **App Content** declarations (Privacy Policy URL, Data Safety, Advertising ID, Content Ratings).
- Invite your 20 testers using their email addresses (via Google Groups or direct email lists).
- Ensure testers opt-in and keep the app installed for 14 days.
- Once the 14 days have passed and testing criteria are met, apply for Production access directly in the Play Console dashboard!

---

## 🐛 Bugs Fixed in this Session
- **GitHub Repository Restructure:** Removed messy legacy folders and normalized the project root to `com.ritikqrscanner.app`.
- **Image Magick Resizing:** Fixed Play Console upload errors by executing ImageMagick commands directly on the host machine to perfectly frame graphics.
- **Gradle Signing:** Configured missing `signingConfigs { release { ... } }` in the gradle script for seamless bundle generation.
- **API 35 Migration:** Handled deprecation warnings and updated the SDK target to meet Google's strict mid-2026 publishing requirements.


Resume: agy --conversation=68c6e9f9-d4e9-497f-859a-cb3dfdb7748b (or -c)

