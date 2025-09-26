package com.cws.printer

import platform.Foundation.NSLog
import kotlin.experimental.ExperimentalNativeApi

actual object ConsoleLogger {

    actual fun v(message: String) {
        NSLog("[VERBOSE] ${tag().className}: $message")
    }

    actual fun i(message: String) {
        NSLog("[INFO] ${tag().className}: $message")
    }

    actual fun d(message: String) {
        NSLog("[DEBUG] ${tag().className}: $message")
    }

    actual fun w(message: String) {
        NSLog("[WARNING] ${tag().className}: $message")
    }

    actual fun e(message: String) {
        NSLog("[ERROR] ${tag().className}: $message")
    }

    actual fun e(message: String, exception: Throwable) {
        NSLog("[ERROR] ${tag().className}: $message")
        exception.printStackTrace()
    }

    @OptIn(ExperimentalNativeApi::class)
    actual fun tag(): Tag {
        val stack = Throwable().getStackTrace()
        val caller = stack.getOrNull(2) ?: ""
        return Tag(caller, "")
    }

}