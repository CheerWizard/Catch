plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("com.android.library")
}

kotlin {
    androidTarget()
    js(IR) {
        browser {
            binaries.library()
        }
        nodejs {
            binaries.library()
        }
        compilerOptions {
            // enable support of BigInt for Long
            freeCompilerArgs.add("-Xir-per-module")
            freeCompilerArgs.add("-Xuse-js-bigint-for-long")
        }
    }
    jvm("desktop")
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.atomicfu)
            }
        }

        val jniMain by creating {
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependsOn(jniMain)
        }

        val desktopMain by getting {
            dependsOn(jniMain)
        }

        val jsMain by getting {
            dependsOn(commonMain)
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        val iosX64Main by getting { dependsOn(nativeMain) }
        val iosArm64Main by getting { dependsOn(nativeMain) }
        val iosSimulatorArm64Main by getting { dependsOn(nativeMain) }
    }
}

android {
    namespace = "com.cws.fmm"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64")
        }
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
            }
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/cpp/cmemory/CMakeLists.txt")
        }
    }
}

val jniBuildDir = file("$buildDir/jni")

fun cmakeTask(project: String, platform: String, generator: String) = tasks.register("buildJni_$platform") {
    group = "jni"
    doLast {
        println("Running cmakeTask for platform:$platform project:$project")

        val outDir = file("$jniBuildDir/$platform")
        outDir.mkdirs()

        val javaHome = System.getenv("JAVA_HOME") ?: "/usr/lib/jvm/java-21-openjdk-amd64"

        val jniPlatformInclude = when(platform) {
            "linux-x86_64" -> "linux"
            "windows-x86_64" -> "win32"
            "macos-x86_64" -> "darwin"
            else -> throw GradleException("Unknown platform")
        }

        val jniIncludeArgs = listOf(
            "-DCMAKE_BUILD_TYPE=Release",
            "-DJAVA_HOME=$javaHome",
            "-DCMAKE_INCLUDE_PATH=$javaHome/include;$javaHome/include/$jniPlatformInclude"
        )

        exec {
            workingDir = outDir
            environment("JAVA_HOME", javaHome)
            println("Running cmake -G $generator")
            commandLine("cmake", "-G", generator, *jniIncludeArgs.toTypedArray(), "../../../src/cpp/$project")
        }

        exec {
            workingDir = outDir
            environment("JAVA_HOME", javaHome)
            println("Running cmake --build .")
            commandLine("cmake", "--build", ".")
        }

        val libName = when(platform) {
            "linux-x86_64" -> "lib$project.so"
            "windows-x86_64" -> "$project.dll"
            "macos-x86_64" -> "lib$project.dylib"
            else -> throw GradleException("Unknown platform")
        }

        copy {
            val fromDir = "$outDir/$libName"
            val toDir = "src/desktopMain/resources/jni/$platform"
            println("Copying $fromDir -> $toDir")
            from(fromDir)
            into(toDir)
        }
    }
}

// Determine platform once during configuration
val osName = System.getProperty("os.name").lowercase()
val osArch = System.getProperty("os.arch").lowercase()

println("OS: $osName, Arch: $osArch")

val generator = when {
    osName.contains("windows") -> "Visual Studio 17 2022"
    osName.contains("linux") -> "Unix Makefiles"
    osName.contains("mac") -> "Unix Makefiles"
    else -> throw GradleException("Unsupported OS: $osName")
}

val platform = when {
    osName.contains("windows") && osArch.contains("64") -> "windows-x86_64"
    osName.contains("linux") && osArch.contains("64") -> "linux-x86_64"
    osName.contains("mac") && osArch.contains("64") -> "macos-x86_64"
    else -> throw GradleException("Unsupported OS/Arch: $osName / $osArch")
}

val cmakeBuild = cmakeTask("cmemory", platform, generator)

tasks.register("buildJni") {
    dependsOn(cmakeBuild)
}