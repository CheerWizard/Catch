plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("com.android.library")
}

kotlin {
    androidTarget()
    js(IR) {
        browser {
            binaries.library()
            commonWebpackConfig {
                cssSupport {
                    enabled = true
                }
            }
        }
        nodejs {
            binaries.library()
        }
    }
    jvm("desktop")
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                // KLog
                implementation(project(":klog"))
                // Compose
                implementation("org.jetbrains.compose.runtime:runtime:1.7.1")
                implementation("org.jetbrains.compose.foundation:foundation:1.7.1")
                // Coroutines and Atomics
                implementation(libs.atomicfu)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation(kotlin("stdlib-common"))
            }
        }

        val composeUiMain by creating {
            dependencies {
                implementation("org.jetbrains.compose.material:material:1.7.1")
                implementation(compose.components.uiToolingPreview)
            }
            dependsOn(commonMain)
        }

        val webUiMain by creating {
            dependencies {
                implementation(compose.html.core)
                implementation(compose.runtime)
            }
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependencies {
                implementation("androidx.activity:activity-compose:1.10.1")
                implementation(libs.androidx.core.ktx)
            }
            dependsOn(composeUiMain)
        }

        val iosMain by creating {
            dependsOn(composeUiMain)
        }

        val desktopMain by getting {
            dependencies {
                api(compose.desktop.currentOs)
            }
            dependsOn(composeUiMain)
        }

        val jsMain by getting {
            dependsOn(webUiMain)
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

android {
    compileSdk = 36
    namespace = "com.cws.kanvas.ui"

    defaultConfig {
        minSdk = 26
        targetSdk = 36
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}