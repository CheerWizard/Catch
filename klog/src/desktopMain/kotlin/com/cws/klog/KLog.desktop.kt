package com.cws.klog

import kotlin.experimental.ExperimentalNativeApi

actual object KLog {

    actual var enabled: Boolean = BuildConfig.DEBUG

    actual fun verbose(message: String) {
        if (enabled) {
            val tag = tag()
            println("[VERBOSE] ${tag.className}.${tag.methodName}: $message")
        }
    }

    actual fun info(message: String) {
        if (enabled) {
            val tag = tag()
            println("[INFO] ${tag.className}.${tag.methodName}: $message")
        }
    }

    actual fun debug(message: String) {
        if (enabled) {
            val tag = tag()
            println("[DEBUG] ${tag.className}.${tag.methodName}: $message")
        }
    }

    actual fun warn(message: String) {
        if (enabled) {
            val tag = tag()
            println("[WARNING] ${tag.className}.${tag.methodName}: $message")
        }
    }

    actual fun error(message: String) {
        if (enabled) {
            val tag = tag()
            System.err.println("[ERROR] ${tag.className}.${tag.methodName}: $message")
        }
    }

    actual fun error(message: String, exception: Throwable) {
        if (enabled) {
            val tag = tag()
            System.err.println("[ERROR] ${tag.className}.${tag.methodName}: $message")
            exception.printStackTrace()
        }
    }

    @OptIn(ExperimentalNativeApi::class)
    actual fun tag(): Tag {
        val stack = Throwable().stackTrace
        val caller = stack.getOrNull(3) ?: return Tag("", "")
        return Tag(caller.className, caller.methodName)
    }

}