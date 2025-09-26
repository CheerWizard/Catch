package com.cws.fmm

actual fun getCurrentThreadID(): Int {
    return 0
}

actual fun getMaxThreadCount(): Int {
    return 1
}