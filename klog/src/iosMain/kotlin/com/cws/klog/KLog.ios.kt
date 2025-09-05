package com.cws.klog

import platform.Foundation.NSLog
import kotlin.experimental.ExperimentalNativeApi

actual object KLog {

    actual var enabled: Boolean = BuildConfig.DEBUG

    actual fun verbose(message: String) {
        if (enabled) {
            NSLog("[VERBOSE] ${tag().className}: $message")
        }
    }

    actual fun info(message: String) {
        if (enabled) {
            NSLog("[INFO] ${tag().className}: $message")
        }
    }

    actual fun debug(message: String) {
        if (enabled) {
            NSLog("[DEBUG] ${tag().className}: $message")
        }
    }

    actual fun warn(message: String) {
        if (enabled) {
            NSLog("[WARNING] ${tag().className}: $message")
        }
    }

    actual fun error(message: String) {
        if (enabled) {
            NSLog("[ERROR] ${tag().className}: $message")
        }
    }

    actual fun error(message: String, exception: Throwable) {
        if (enabled) {
            NSLog("[ERROR] ${tag().className}: $message")
            exception.printStackTrace()
        }
    }

    @OptIn(ExperimentalNativeApi::class)
    actual fun tag(): Tag {
        val stack = Throwable().getStackTrace()
        val caller = stack.getOrNull(2) ?: ""
        return Tag(caller, "")
    }

}