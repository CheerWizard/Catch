plugins {
    id("org.jetbrains.kotlin.multiplatform") version "2.2.10"
}

kotlin {
    jvm()
    js(IR) {
        browser()
        nodejs()
    }
//    iosArm64()
//    iosX64()
//    iosSimulatorArm64()
    linuxX64()
//    mingwX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.atomicfu)
            }
        }
        val commonTest by getting

        val jvmMain by getting {
            dependencies {
                implementation(libs.symbol.processing.api)
                implementation(libs.squareup.kotlinpoet)
                implementation(libs.kotlinpoet.ksp)
            }
        }
        val jvmTest by getting

        val jsMain by getting {
            dependencies {}
        }
        val jsTest by getting
    }
}