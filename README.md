# Rider TV Native Android App

![Android TV](https://img.shields.io/badge/Platform-Android%20TV-green?style=for-the-badge&logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-17-blue?style=for-the-badge&logo=kotlin)
![Compose](https://img.shields.io/badge/Compose-TV-orange?style=for-the-badge&logo=jetpackcompose)

A native Android TV application for Rider TV, built with Jetpack Compose for TV and modern media playback libraries.

## 🚀 Features

- **Jetpack Compose for TV**: Optimized 10-foot UI experience.
- **ExoPlayer Integration**: High-performance HLS/VOD streaming.
- **Supabase Integration**: Real-time sync with the Rider TV management portal.
- **Native Performance**: Built from the ground up for low-latency navigation.

## 🛠️ Tech Stack

- **UI**: Jetpack Compose for TV (Material 3).
- **Network**: Retrofit, OkHttp, Ktor (for Supabase).
- **Backend/Auth**: Supabase.
- **Media**: Media3 ExoPlayer.
- **Image Loading**: Coil.

## 📋 Prerequisites

- **Android Studio**: Ladybug or later recommended.
- **JDK**: Version 17.
- **SDK**: Android SDK 35 (Compile), 26 (Min).

## 💻 Testing with Android Studio

To test the application directly from **Android Studio**, follow these steps:

### 1. Open the Project
1. Start **Android Studio**.
2. Select **Open** (or *File > Open*).
3. Navigate to and select the folder: `c:\Users\Esteban\.gemini\antigravity\scratch\rider-tv-native`.
4. Wait for the **Gradle sync** to complete (check the progress bar in the bottom right corner).

### 2. Set Up a Test Device
For a TV app, you need an **Android TV Emulator** or a physical TV device:
- **Using Emulator**: Go to *Tools > Device Manager* > *Create Device* > Select the **TV** category > Choose a profile (e.g., **Android TV 1080p**) and download a system image (API 33+ recommended).
- **Using Physical TV**: Enable *Developer Options* and *ADB Debugging* on your TV, then connect via USB or Network (IP).

### 3. Run the App
1. Ensure the **`app`** module is selected in the top bar.
2. Select your device (emulator or physical TV).
3. Click the **Run** (green "Play" icon) or press `Shift + F10`.

### 4. Debugging
- Use the **Debug** (green bug icon) to step through code.
- Monitor logs in the **Logcat** tab at the bottom for real-time app events and Supabase logs.

---
© 2025 Rider TV Premium Services.
