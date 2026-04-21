plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.rider.tv"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rider.tv"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0-NATIVE"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2025.01.01")
    implementation(composeBom)

    // Android TV Compose
    implementation("androidx.tv:tv-foundation:1.0.0-rc01")
    implementation("androidx.tv:tv-material:1.1.0-rc01")
    implementation("androidx.compose.material3:material3")

    // Core Android
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.0")

    // Media Player (ExoPlayer)
    implementation("androidx.media3:media3-exoplayer:1.5.1")
    implementation("androidx.media3:media3-exoplayer-hls:1.5.1")
    implementation("androidx.media3:media3-ui:1.5.1")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Supabase
    implementation("io.github.jan-tennert.supabase:postgrest-kt:3.0.3")
    implementation("io.github.jan-tennert.supabase:auth-kt:3.0.3")
    implementation("io.ktor:ktor-client-okhttp:3.0.3")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Image loading
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Icons
    implementation("androidx.compose.material:material-icons-extended")
}
