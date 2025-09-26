package com.cws.fmm

actual fun getCurrentThreadID(): Int {
    return Thread.currentThread().id.toInt()
}

actual fun getMaxThreadCount(): Int {
    return Runtime.getRuntime().availableProcessors() * 2
}