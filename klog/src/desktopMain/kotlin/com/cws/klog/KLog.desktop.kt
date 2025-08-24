package com.cws.klog

import kotlin.experimental.ExperimentalNativeApi

actual object KLog {

    actual var enabled: Boolean = BuildConfig.DEBUG

    actual fun verbose(message: String) {
        if (enabled) {
            println("[VERBOSE] ${tag()}: $message")
        }
    }

    actual fun info(message: String) {
        if (enabled) {
            println("[INFO] ${tag()}: $message")
        }
    }

    actual fun debug(message: String) {
        if (enabled) {
            println("[DEBUG] ${tag()}: $message")
        }
    }

    actual fun warn(message: String) {
        if (enabled) {
            println("[WARNING] ${tag()}: $message")
        }
    }

    actual fun error(message: String) {
        if (enabled) {
            println("[ERROR] ${tag()}: $message")
        }
    }

    actual fun error(message: String, exception: Throwable) {
        if (enabled) {
            println("[ERROR] ${tag()}: $message")
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