plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    id("com.android.library")
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
                // Ktor
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content)
                implementation(libs.ktor.serialization.kotlinx.json)
                // Kotlin standard
                implementation(libs.kotlinx.datetime)
                implementation(libs.atomicfu)
                implementation(libs.kotlinx.coroutines.core)
                implementation(kotlin("stdlib-common"))
            }
            kotlin.srcDir("build/generated/src/commonMain/kotlin")
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
            dependsOn(commonMain)
        }

        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.java)
            }
            dependsOn(commonMain)
        }

        val nativeMain by creating {
            dependencies {
                implementation(libs.ktor.client.curl)
            }
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
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
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