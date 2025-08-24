package com.cws.kmemory

actual fun getCurrentThreadID(): Int {
    return 0
}

actual fun getMaxThreadCount(): Int {
    return 1
}