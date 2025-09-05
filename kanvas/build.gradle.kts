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
                api(project(":klog"))
                api(project(":kmemory"))
                // Compose
                api("org.jetbrains.compose.runtime:runtime:1.7.1")
                api("org.jetbrains.compose.foundation:foundation:1.7.1")
                api("org.jetbrains.compose.material:material:1.7.1")
                api(compose.components.uiToolingPreview)
                // Coroutines and Atomics
                api(libs.atomicfu)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                api(kotlin("stdlib-common"))
            }
        }

        val composeUiMain by creating {
            dependsOn(commonMain)
        }

        val webUiMain by creating {
            dependencies {
                api("org.jetbrains.skiko:skiko-js:0.8.9")
                api(compose.web.core)
            }
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependencies {
                // Compose
                api("androidx.activity:activity-compose:1.10.1")
                api(libs.androidx.core.ktx)
            }
            dependsOn(composeUiMain)
        }

        val iosMain by creating {
            dependsOn(composeUiMain)
        }

        val desktopMain by getting {
            dependencies {
                // Compose
                api(compose.desktop.currentOs)

                val lwjglVersion = "3.3.6"
                val lwjglNatives = when (System.getProperty("os.name").lowercase()) {
                    "linux" -> "natives-linux"
                    "windows" -> "natives-windows"
                    "mac os x" -> "natives-macos"
                    else -> throw GradleException("Unsupported OS")
                }

                // LWJGL core
                implementation("org.lwjgl:lwjgl:$lwjglVersion")
                implementation("org.lwjgl:lwjgl-opengl:$lwjglVersion")
                implementation("org.lwjgl:lwjgl-glfw:$lwjglVersion")
                implementation("org.lwjgl:lwjgl-stb:$lwjglVersion")

                // LWJGL natives
                runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
                runtimeOnly("org.lwjgl:lwjgl-opengl:$lwjglVersion:$lwjglNatives")
                runtimeOnly("org.lwjgl:lwjgl-glfw:$lwjglVersion:$lwjglNatives")
                runtimeOnly("org.lwjgl:lwjgl-stb:$lwjglVersion:$lwjglNatives")
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
    namespace = "com.cws.kanvas"

    defaultConfig {
        minSdk = 26
        targetSdk = 36
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}