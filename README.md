<div align="center">

# 🌍 Arsad - أرصاد
**The Intelligent Weather Companion for a Seamless Daily Life**

[![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Koin](https://img.shields.io/badge/Koin-FFEB3B?style=for-the-badge&logo=koin&logoColor=black)](https://insert-koin.io/)

</div>

---

## 📱 About the App

**Arsad** (أرصاد) is a modern, high-performance weather application built from the ground up with **Jetpack Compose**. It provides a fluid user experience with native support for **Arabic and English** (RTL/LTR), ensuring that weather data is not just accurate, but also accessible and beautiful.

---
## 📸 Screenshots

Add your app screenshots here to showcase the features:

<img width="1920" height="1080" alt="Purple Pink Gradient Mobile Application Presentation" src="https://github.com/user-attachments/assets/d4b52e53-cb0b-4ab4-a045-b7a16bf68ca3" />
<img width="1920" height="1080" alt="Purple Pink Gradient Mobile Application Presentation (1)" src="https://github.com/user-attachments/assets/33da1c2b-0d8b-415d-8e88-07f26cda7db6" />
<img width="1920" height="1080" alt="Purple Pink Gradient Mobile Application Presentation (2)" src="https://github.com/user-attachments/assets/b3b566dd-8eba-41c1-b53f-d00b5520514e" />

## ✨ Key Features

### 🌍 Core Features
- **Smart Location Detection**: Full GPS support with multiple location options
- **Modern Interface**: Built entirely with Jetpack Compose
- **Multi-Language Support**: Full Arabic and English support with responsive UI (RTL/LTR)
- **Real-Time Updates**: Automatic weather data synchronization every hour
- **Smart Notifications**: Weather alerts and important warnings
- **Home Screen Widget**: Display weather directly on your home screen

### 🎨 User Experience
- **Dark and Light Theme**: Full support for night and day modes
- **Smooth Design**: Easy and intuitive interface
- **Detailed View**: Accurate weather details including humidity and wind speeds
- **Interactive Map**: Select locations from the map

### 🔧 Advanced Technologies
- **Jetpack Compose**: Modern and efficient UI building
- **Kotlin Coroutines**: Professional asynchronous handling
- **WorkManager**: Smart background data synchronization
- **Room Database**: Local data storage
- **KSP (Kotlin Symbol Processing)**: Fast symbol processing

---

## 🛠️ Requirements

### Technology Stack
- **Language**: Kotlin
- **Android SDK**: API 24 (Android 7.0) - API 36 (Android 15)
- **Build System**: Gradle (KTS)
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Repository Pattern
- **Dependency Injection**: Koin
- **Database**: Room
- **Background Tasks**: WorkManager

### Core Dependencies
```gradle
- Jetpack Compose (UI Toolkit)
- Jetpack Core & Activity
- Kotlin Coroutines
- KSP for Annotation Processing
- WorkManager for Background Sync
- Room Database
- Koin for Dependency Injection
```

---

## 📦 Project Structure

```
Arsad/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/arsad/
│   │   │   │   ├── data/              # Data Layer
│   │   │   │   │   ├── local/         # Local Database
│   │   │   │   │   ├── remote/        # API & Remote Services
│   │   │   │   │   ├── repository/    # Repository Pattern
│   │   │   │   │   ├── location/      # Location Services
│   │   │   │   │   ├── worker/        # Background Workers
│   │   │   │   │   ├── models/        # Data Models
│   │   │   │   │   └── mapper/        # Data Transformation
│   │   │   │   ├── presentation/      # Presentation Layer
│   │   │   │   │   ├── home/          # Home Screen
│   │   │   │   │   ├── alerts/        # Alerts Screen
│   │   │   │   │   ├── saved/         # Saved Locations
│   │   │   │   │   ├── settings/      # Settings
│   │   │   │   │   ├── weather_details/ # Weather Details
│   │   │   │   │   ├── map_picker/    # Location Picker
│   │   │   │   │   ├── components/    # Shared Components
│   │   │   │   │   ├── navigation/    # Navigation File
│   │   │   │   │   ├── glance_app_widget/ # Home Widget
│   │   │   │   │   └── splash/        # Splash Screen
│   │   │   │   ├── ui/                # UI Components
│   │   │   │   │   ├── theme/         # Themes & Colors
│   │   │   │   │   └── components/    # Reusable Components
│   │   │   │   ├── util/              # Helper Functions
│   │   │   │   ├── di/                # Dependency Injection Setup
│   │   │   │   └── MainActivity.kt    # Main Activity
│   │   │   ├── res/                   # Resources
│   │   │   └── AndroidManifest.xml    # App Manifest
│   │   ├── test/                      # Unit Tests
│   │   └── androidTest/               # Android Tests
│   └── build.gradle.kts               # Build Configuration
├── gradle/                            # Gradle Settings
├── build.gradle.kts                   # Main Build File
├── settings.gradle.kts                # Multi-Module Settings
└── README.md                          # This File
```

---

## 🚀 Quick Start

### Prerequisites
- **Android Studio** (latest version)
- **JDK 11** or higher
- **Android SDK** with API Level 36

### Installation Steps

1. **Clone the Repository**
```bash
git clone https://github.com/yourusername/Arsad.git
cd Arsad
```

2. **Open the Project**
```bash
# In Android Studio:
File → Open → Select Arsad folder
```

3. **Build the Project**
```bash
./gradlew build
```

4. **Run the App**
```bash
./gradlew installDebug
# Or from Android Studio: Run → Run 'app'
```

---

## 📱 Detailed Features

### 🏠 Home Screen
- Current weather display with full details
- Humidity and wind speed information
- 5-day weather forecasts
- Actual and "feels like" temperature

### 🚨 Alerts
- Critical weather alerts
- Hurricane and storm warnings
- Customizable notifications

### 💾 Saved Locations
- Save multiple favorite locations
- Quick switching between locations
- Delete old locations

### ⚙️ Settings
- Language selection (Arabic/English)
- Theme toggle (Light/Dark)
- Temperature unit selection (Celsius/Fahrenheit)
- Wind unit configuration
- Notification toggle

### 🗺️ Location Picker
- Select location from interactive map
- Search location by address
- Use GPS for current location

### 📲 Home Screen Widget
- Display current weather on home screen
- Real-time updates
- Compact and elegant design

---

## 🔐 Security & Permissions

### Required Permissions
```xml
- ACCESS_FINE_LOCATION: For precise GPS location
- ACCESS_COARSE_LOCATION: For approximate location
- INTERNET: For connecting to weather servers
- POST_NOTIFICATIONS: For sending notifications
- SYSTEM_ALERT_WINDOW: For displaying popups
- WRITE_EXTERNAL_STORAGE: For saving data
- ACCESS_NETWORK_STATE: For checking internet connection
```

---

## 📊 Data & Storage

### Local Storage
- **Room Database**: Store weather info and saved locations
- **DataStore**: Save preferences and settings

### Synchronization
- **WorkManager**: Sync data every hour
- **Coroutines**: Asynchronous data processing
- **Flow**: Stream changing data

---

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run Android tests
./gradlew connectedAndroidTest
```

---

## 📈 Performance

- **Lazy Loading**: Load data on demand
- **Caching**: Cache frequently used data
- **Image Optimization**: Optimize images for better performance
- **Memory Management**: Efficient memory management

---

## 🔄 Updates & Maintenance

### Current Version
- **Version**: 1.0
- **Release Date**: March 2026
- **Android Target**: API 36 (Android 15)


## 🤝 Contributing

We welcome your contributions! Please follow these steps:

1. Fork the project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 📧 Contact & Support

- **Email**: ahmdnajkh71@gmail.com

---

## 🙏 Special Thanks

Special thanks to:
- **Google Android Team** for Jetpack Compose
- **Google Cloud** for cloud services
- **OpenWeatherMap** for weather data
- All contributors and testers

---

## 📚 Resources & References

### Official Documentation
- [Android Developers Documentation](https://developer.android.com)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-overview.html)
- [Room Database](https://developer.android.com/training/data-storage/room)

### Helpful Articles
- [MVVM Architecture in Android](https://developer.android.com/jetpack/docs/architecture)
- [Background Tasks with WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- [Compose State Management](https://developer.android.com/codelabs/jetpack-compose-state)

---

## 📄 Important Files

| File | Description |
|------|-------------|
| `MainActivity.kt` | Main activity and app configuration |
| `build.gradle.kts` | Project settings and libraries |
| `AndroidManifest.xml` | App manifest and permissions |
| `gradle/libs.versions.toml` | Library version management |

---

<div align="center">

### Made with ❤️ using Jetpack Compose & Kotlin

⭐ **If you like this project, give it a star!** ⭐

</div>

