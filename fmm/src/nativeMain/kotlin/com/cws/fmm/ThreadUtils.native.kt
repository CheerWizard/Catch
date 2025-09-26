package com.cws.fmm

import platform.posix.pthread_getconcurrency
import platform.posix.pthread_self
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.identityHashCode

@OptIn(ExperimentalNativeApi::class)
actual fun getCurrentThreadID(): Int {
    return pthread_self()?.identityHashCode() ?: 0
}

actual fun getMaxThreadCount(): Int {
    return pthread_getconcurrency()
}