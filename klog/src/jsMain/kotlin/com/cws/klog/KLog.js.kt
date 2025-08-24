package com.cws.klog

actual object KLog {

    actual var enabled: Boolean = BuildConfig.DEBUG

    actual fun verbose(message: String) {
        if (enabled) {
            console.info("[VERBOSE] ${tag()}: $message")
        }
    }

    actual fun info(message: String) {
        if (enabled) {
            console.info("[INFO] ${tag()}: $message")
        }
    }

    actual fun debug(message: String) {
        if (enabled) {
            console.info("[DEBUG] ${tag()}: $message")
        }
    }

    actual fun warn(message: String) {
        if (enabled) {
            console.warn("[WARNING] ${tag()}: $message")
        }
    }

    actual fun error(message: String) {
        if (enabled) {
            console.error("[ERROR] ${tag()}: $message")
        }
    }

    actual fun error(message: String, exception: Throwable) {
        if (enabled) {
            console.error("[ERROR] ${tag()}: $message")
            console.error(exception.message)
        }
    }

    actual fun tag(): String {
        val stack = (js("new Error().stack") as String).lines()
        val caller = stack.getOrNull(3) ?: "unknown"
        return caller
    }

}