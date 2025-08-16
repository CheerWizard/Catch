package com.cws.nativeksp

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class HeapData(
    val autoCreate: Boolean = false
)