package com.cws.kmemory

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class NativeData(
    val autoCreate: Boolean = false,
    val gpuAlignment: Boolean = false
)