plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    id("com.android.library")
}

kotlin {
    androidTarget()
    jvm("desktop")
    js(IR) {
        browser {
            binaries.library()
        }
        nodejs {
            binaries.library()
        }
    }
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {}
            kotlin.srcDir("build/generated/src/commonMain/kotlin")
        }

        val androidMain by getting {
            dependencies {}
        }

        val jsMain by getting {
            dependencies {}
        }

        val iosMain by creating {
            dependsOn(commonMain)
        }

        val desktopMain by getting {
            dependsOn(commonMain)
        }

        val iosX64Main by getting { dependsOn(iosMain) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
    }
}

android {
    compileSdk = 36
    namespace = "com.cws.klog"

    defaultConfig {
        minSdk = 26
        targetSdk = 36
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

val generateBuildConfig by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/src/commonMain/kotlin")
    val packageName = "com.cws.klog"
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