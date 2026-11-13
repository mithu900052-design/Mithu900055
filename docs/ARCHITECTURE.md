# MONEXA MONITOR — System Architecture & Implementation Plan

## Overview
MONEXA MONITOR is an enterprise-grade, consent-based Android device monitoring platform with a paired React/TypeScript Web Admin Command Center.

## Phase 1 Deliverables
1. **Android Customer App (Kotlin + Jetpack Compose + Material 3)**
   - Package: `com.example` (Application ID: `com.aistudio.monexamonitor.hqvxrk`)
   - Custom cybernetic radar/shield adaptive launcher icon
   - MVVM architecture with Kotlin Coroutines & Flow
   - On-device hardware telemetry reader (Battery, Storage, Network, OS version)
   - 6-digit numeric Device ID generation with uniqueness guarantee
   - Real-time customer pairing approval/rejection and access revocation flow
   - Full bilingual support (English & Bengali)
   - Futuristic dark and light themes

2. **Web Admin Dashboard (React + TypeScript + Tailwind CSS)**
   - Located at `/admin-dashboard/`
   - Fleet metrics dashboard (Active nodes, connected, offline, pending approval)
   - 6-Digit Device ID pairing modal
   - MONEXA LIVE SCREEN VIEWER with independent pan/zoom canvas and touch-isolation guarantee
   - 12-section device command center

3. **Backend & Cloud Database (Firebase Firestore)**
   - Project ID: `gen-lang-client-0977118472`
   - Database ID: `ai-studio-android-monexamo-5938685a-a859-490b-902c-2090e00ca09f`
   - Intermediate blueprint schema: `firebase-blueprint.json`
   - Zero-trust security rules: `firestore.rules` deployed to backend

## Complete Directory Structure
```
/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/
│   │   │   ├── MainActivity.kt
│   │   │   ├── data/
│   │   │   │   ├── model/
│   │   │   │   │   ├── Device.kt
│   │   │   │   │   └── MonexaLanguage.kt
│   │   │   │   └── repository/
│   │   │   │       └── MonexaDeviceRepository.kt
│   │   │   └── ui/
│   │   │       ├── components/
│   │   │       │   ├── DeviceIdCard.kt
│   │   │       │   ├── MonexaHeader.kt
│   │   │       │   ├── PairingApprovalDialog.kt
│   │   │       │   └── TelemetryCards.kt
│   │   │       ├── screens/
│   │   │       │   └── MonexaHomeScreen.kt
│   │   │       └── theme/
│   │   │           ├── Color.kt
│   │   │           ├── Theme.kt
│   │   │           └── Type.kt
│   │   └── res/
│   │       ├── drawable/
│   │       │   ├── ic_launcher_background.xml
│   │       │   └── ic_launcher_foreground.xml
│   │       ├── mipmap-*/
│   │       └── values/
│   │           ├── firebase_applet_config.xml
│   │           ├── strings.xml
│   │           └── themes.xml
│   ├── google-services.json
│   └── build.gradle.kts
├── admin-dashboard/
│   ├── src/
│   │   ├── components/
│   │   │   ├── DeviceCard.tsx
│   │   │   ├── LiveScreenViewer.tsx
│   │   │   ├── Navbar.tsx
│   │   │   └── PairingModal.tsx
│   │   ├── pages/
│   │   │   ├── DashboardPage.tsx
│   │   │   └── DeviceDetailPage.tsx
│   │   ├── services/
│   │   │   └── firebase.ts
│   │   ├── types/
│   │   │   └── monexa.ts
│   │   ├── App.tsx
│   │   ├── index.css
│   │   └── main.tsx
│   ├── index.html
│   ├── package.json
│   ├── tailwind.config.js
│   ├── tsconfig.json
│   └── vite.config.ts
├── firebase-blueprint.json
├── firestore.rules
├── firebase.json
└── metadata.json
```
