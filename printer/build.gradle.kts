plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    id("com.android.library")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
}

kotlin {
    androidTarget()
    jvm("jvm")
    js(IR) {
        browser {
            binaries.library()
        }
        nodejs {
            binaries.library()
        }
    }
    mingwX64()
    linuxX64()
    macosX64()
    macosArm64()
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Crashlytics
                implementation("com.google.firebase:firebase-crashlytics-ktx:18.6.2")
                implementation("com.google.firebase:firebase-analytics-ktx:21.6.2")
                // Kotlin standard
                implementation(libs.kotlinx.datetime)
                implementation(libs.atomicfu)
                implementation(libs.kotlinx.coroutines.core)
                implementation(kotlin("stdlib-common"))
            }
            kotlin.srcDir("build/generated/src/commonMain/kotlin")
        }

        val androidMain by getting {
            dependsOn(commonMain)
        }

        val jsMain by getting {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(commonMain)
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }
        val mingwX64Main by getting {
            dependsOn(nativeMain)
        }
        val linuxX64Main by getting {
            dependsOn(nativeMain)
        }
        val macosX64Main by getting {
            dependsOn(nativeMain)
        }
        val macosArm64Main by getting {
            dependsOn(nativeMain)
        }

        val iosMain by creating {
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

android {
    compileSdk = 36
    namespace = "com.cws.printer"

    defaultConfig {
        minSdk = 26
        targetSdk = 36
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

cocoapods {
    summary = "Printer"
    homepage = "https://printer.cws.com"
    ios.deploymentTarget = "14.0"
    pod("FirebaseCrashlytics")
    pod("FirebaseAnalytics")
}

val generateBuildConfig by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/src/commonMain/kotlin")
    val packageName = "com.cws.printer"
    val build = project.properties["buildConfig.build"]?.toString()?.lowercase()
    println("Build: $build")
    val isDebug = build == "debug"

    outputs.dir(outputDir)

    val file = outputDir.get().file("$packageName/BuildConfig.kt").asFile
    file.parentFile.mkdirs()
    file.writeText("""
            package $packageName

            object BuildConfig {
                const val DEBUG = $isDebug
            }
        """.trimIndent())
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
    dependsOn(generateBuildConfig)
}

afterEvaluate {
    tasks.named("compileKotlinJs") {
        dependsOn(generateBuildConfig)
    }
}