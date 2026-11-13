# MONEXA MONITOR — Web Admin Dashboard

Production-quality Web Command Center for the MONEXA MONITOR platform, built with React, TypeScript, and Tailwind CSS.

## Features
- **Live Fleet Telemetry:** Real-time monitoring of all registered Android devices.
- **6-Digit Pairing System:** Add devices by their 6-digit numeric ID; triggers consent handshake on customer phone.
- **MONEXA LIVE SCREEN VIEWER:** Miracast-like live screen streaming with independent viewer pan/zoom controls and touch-isolation guarantee.
- **12-Section Device Detail Inspector:** Overview, Device Info, Live Location, Location History, App Usage, Screen Time, Notifications, Shared Media, Live Screen Viewer, Reports, Privacy & Permissions, Connection Logs.
- **Bilingual Interface:** Instant switching between English and Bengali (বাংলা).

## Technology Stack
- React 18 + Vite
- TypeScript
- Tailwind CSS
- Firebase Firestore SDK
- Lucide React

## Setup & Running
```bash
cd admin-dashboard
npm install
npm run dev
```

The dashboard runs locally on `http://localhost:3000`.

## Production Build
```bash
npm run build
```
Generates production assets in `admin-dashboard/dist/`.
