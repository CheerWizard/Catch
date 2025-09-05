import com.google.devtools.ksp.gradle.KspTask
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("com.android.application")
    id("com.google.devtools.ksp") version "2.2.10-2.0.2"
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
        release {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {}
}

kotlin {
    androidTarget()
    jvm("desktop")
    js(IR) {
        browser {
            binaries.executable()
        }
        nodejs {
            binaries.executable()
        }
    }
//    iosArm64()
//    iosX64()
//    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            ksp { arg("target", "common") }
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                implementation(project(":kanvas"))
            }
        }

        val androidMain by getting {
            ksp { arg("target", "android") }
            kotlin.srcDir("build/generated/ksp/android/androidMain/kotlin")
            dependsOn(commonMain)
        }

        val iosMain by creating {
            ksp { arg("target", "ios") }
            kotlin.srcDir("build/generated/ksp/ios/iosMain/kotlin")
            dependsOn(commonMain)
        }

        val desktopMain by getting {
            ksp { arg("target", "desktop") }
            kotlin.srcDir("build/generated/ksp/desktop/desktopMain/kotlin")
            dependsOn(commonMain)
        }

        val jsMain by getting {
            ksp { arg("target", "js") }
            kotlin.srcDir("build/generated/ksp/js/jsMain/kotlin")
            dependsOn(commonMain)
        }
    }
}

dependencies {
    ksp(project(":kmemory-proc"))
    implementation(project(":kmemory-proc"))
}