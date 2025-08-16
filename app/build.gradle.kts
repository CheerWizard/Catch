import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
}

val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(rootProject.file("keystore.properties")))

android {
    namespace = "com.cws.acatch"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.cws.acatch"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["KEY_ALIAS"] as String
            keyPassword = keystoreProperties["KEY_PASSWORD"] as String
            storeFile = file(keystoreProperties["STORE_FILE"] as String)
            storePassword = keystoreProperties["STORE_PASSWORD"] as String
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isDebuggable = false
            // TODO fix code obfuscation for generics
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += listOf("-Xdebug")
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    // Native KSP
    implementation(kotlin("stdlib"))
    ksp(project(":nativeksp"))
    api(project(":nativeksp"))

    // Image Loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.svg)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.converter.scalars)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)

    // Logging
    implementation(libs.timber)

    // Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // UI
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    api(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}