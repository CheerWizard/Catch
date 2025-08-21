plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    id("com.android.library")
}

kotlin {
    js(IR) {
        browser()
    }
    androidTarget()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":kmemory"))
                implementation(kotlin("stdlib-common"))
                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.core.ktx)
            }
        }

//        val iosMain by getting {
//            dependencies {
//
//            }
//        }

        val desktopMain by getting {
            dependencies {
                implementation("org.lwjgl:lwjgl:3.3.6")
                implementation("org.lwjgl:lwjgl-opengl:3.3.6")
                implementation("org.lwjgl:lwjgl-glfw:3.3.6")
                implementation("org.lwjgl:lwjgl-stb:3.3.6")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-browser:0.16.0")
            }
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