package com.cws.fmm

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class FastObject(
    val gpuAlignment: Boolean = false
)