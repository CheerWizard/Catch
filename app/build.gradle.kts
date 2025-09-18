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

kotlin {
    androidTarget()

    jvm("desktop")

    js(IR) {
        browser {
            binaries.executable()
            webpackTask {
                copy {
                    from("$projectDir/src/commonMain/resources")
                    into("$buildDir/processedResources/js/main")
                }
            }
        }
        nodejs {
            binaries.executable()
        }
    }

    iosArm64 {
        binaries {
            framework {
                baseName = "Catch"
            }
        }
    }

    iosX64 {
        binaries {
            framework {
                baseName = "Catch"
            }
        }
    }

    iosSimulatorArm64 {
        binaries {
            framework {
                baseName = "Catch"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                // Networking
                implementation(libs.kotlinx.serialization.json)

                implementation(project(":kanvas"))
                implementation(project(":kmemory"))
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
        }

        val iosMain by creating {
            dependsOn(commonMain)
        }

        val desktopMain by getting {
            dependsOn(commonMain)
        }

        val jsMain by getting {
            dependsOn(commonMain)
        }

        val iosX64Main by getting {
            dependsOn(iosMain)
        }

        val iosArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

    }
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

    sourceSets["main"].assets.srcDir("$buildDir/generated/commonAssets")
}

dependencies {
    ksp(project(":kmemory-proc"))
}

afterEvaluate {
    tasks.named("kspDebugKotlinAndroid") {
        enabled = false
    }
    tasks.named("kspReleaseKotlinAndroid") {
        enabled = false
    }
    tasks.named("kspKotlinDesktop") {
        enabled = false
    }
    tasks.named("kspKotlinJs") {
        enabled = false
    }
}

tasks.register<Copy>("copyCommonResourcesToAssets") {
    from("src/commonMain/resources")
    into("$buildDir/generated/commonAssets")
}

tasks.named("preBuild") {
    dependsOn("copyCommonResourcesToAssets")
}

tasks.register("ksp") {
    dependsOn("kspCommonMainKotlinMetadata")
}