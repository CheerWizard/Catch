plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("com.android.library")
    id("com.google.devtools.ksp") version "2.2.10-2.0.2"
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
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                // DI
                api(libs.koin.core)
                api(libs.koin.compose)
                api(libs.koin.compose.viewModel)
                //
                api(project(":klog"))
                api(project(":kmemory"))
                // Compose
                api("org.jetbrains.compose.runtime:runtime:1.7.1")
                api("org.jetbrains.compose.foundation:foundation:1.7.1")
                // Coroutines and Atomics
                api(libs.atomicfu)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                api(kotlin("stdlib-common"))
            }
        }

        val composeUiMain by creating {
            dependencies {
                // Compose
                api("org.jetbrains.compose.material:material:1.7.1")
                api(compose.components.uiToolingPreview)
            }
            dependsOn(commonMain)
        }

        val webUiMain by creating {
            dependencies {
                api(compose.html.core)
                api(compose.runtime)
            }
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependencies {
                // DI
                api(libs.koin.android)
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
                api("org.lwjgl:lwjgl:$lwjglVersion")
                api("org.lwjgl:lwjgl-opengl:$lwjglVersion")
                api("org.lwjgl:lwjgl-glfw:$lwjglVersion")
                api("org.lwjgl:lwjgl-stb:$lwjglVersion")

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