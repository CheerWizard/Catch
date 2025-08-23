package com.cws.klog

import kotlin.experimental.ExperimentalNativeApi

actual object KLog {

    actual fun verbose(message: String) {
        println("[VERBOSE] ${tag()}: $message")
    }

    actual fun info(message: String) {
        println("[INFO] ${tag()}: $message")
    }

    actual fun debug(message: String) {
        println("[DEBUG] ${tag()}: $message")
    }

    actual fun warn(message: String) {
        println("[WARNING] ${tag()}: $message")
    }

    actual fun error(message: String) {
        println("[ERROR] ${tag()}: $message")
    }

    actual fun error(message: String, exception: Throwable) {
        println("[ERROR] ${tag()}: $message")
        exception.printStackTrace()
    }

    @OptIn(ExperimentalNativeApi::class)
    actual fun tag(): String {
        val stack = Throwable().getStackTrace()
        val caller = stack.getOrNull(3) ?: "Unknown"
        return caller
    }

}