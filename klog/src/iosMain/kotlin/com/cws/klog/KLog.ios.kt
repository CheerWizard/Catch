package com.cws.klog

import platform.Foundation.NSLog
import kotlin.experimental.ExperimentalNativeApi

actual object KLog {

    actual fun verbose(message: String) {
        NSLog("[VERBOSE] ${tag()}: $message")
    }

    actual fun info(message: String) {
        NSLog("[INFO] ${tag()}: $message")
    }

    actual fun debug(message: String) {
        NSLog("[DEBUG] ${tag()}: $message")
    }

    actual fun warn(message: String) {
        NSLog("[WARNING] ${tag()}: $message")
    }

    actual fun error(message: String) {
        NSLog("[ERROR] ${tag()}: $message")
    }

    actual fun error(message: String, exception: Throwable) {
        NSLog("[ERROR] ${tag()}: $message")
        exception.printStackTrace()
    }

    @OptIn(ExperimentalNativeApi::class)
    actual fun tag(): String {
        val stack = Throwable().getStackTrace()
        val caller = stack.getOrNull(3) ?: "Unknown"
        return caller
    }

}