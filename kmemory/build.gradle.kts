plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()
    js(IR) {
        browser()
        nodejs()
    }
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    linuxX64()
    mingwX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.squareup.kotlinpoet)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.atomicfu)
            }
        }

        val jvmMain by getting {
            dependencies {}
        }

        val jsMain by getting {
            dependencies {}
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        val iosX64Main by getting { dependsOn(nativeMain) }
        val iosArm64Main by getting { dependsOn(nativeMain) }
        val iosSimulatorArm64Main by getting { dependsOn(nativeMain) }
        val linuxX64Main by getting { dependsOn(nativeMain) }
        val mingwX64Main by getting { dependsOn(nativeMain) }
    }
}