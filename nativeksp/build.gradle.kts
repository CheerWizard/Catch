plugins {
    kotlin("jvm") version "2.2.10"
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.symbol.processing.api)
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
}