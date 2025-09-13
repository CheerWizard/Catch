package com.cws.kmemory

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class FastObject(
    val gpuAlignment: Boolean = false
)