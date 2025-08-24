package com.cws.klog

import platform.Foundation.NSLog
import kotlin.experimental.ExperimentalNativeApi

actual object KLog {

    actual var enabled: Boolean = BuildConfig.DEBUG

    actual fun verbose(message: String) {
        if (enabled) {
            NSLog("[VERBOSE] ${tag()}: $message")
        }
    }

    actual fun info(message: String) {
        if (enabled) {
            NSLog("[INFO] ${tag()}: $message")
        }
    }

    actual fun debug(message: String) {
        if (enabled) {
            NSLog("[DEBUG] ${tag()}: $message")
        }
    }

    actual fun warn(message: String) {
        if (enabled) {
            NSLog("[WARNING] ${tag()}: $message")
        }
    }

    actual fun error(message: String) {
        if (enabled) {
            NSLog("[ERROR] ${tag()}: $message")
        }
    }

    actual fun error(message: String, exception: Throwable) {
        if (enabled) {
            NSLog("[ERROR] ${tag()}: $message")
            exception.printStackTrace()
        }
    }

    @OptIn(ExperimentalNativeApi::class)
    actual fun tag(): String {
        val stack = Throwable().getStackTrace()
        val caller = stack.getOrNull(3) ?: "Unknown"
        return caller
    }

}