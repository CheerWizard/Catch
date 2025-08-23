package com.cws.klog

actual object KLog {

    actual fun verbose(message: String) {
        console.info("[VERBOSE] ${tag()}: $message")
    }

    actual fun info(message: String) {
        console.info("[INFO] ${tag()}: $message")
    }

    actual fun debug(message: String) {
        console.info("[DEBUG] ${tag()}: $message")
    }

    actual fun warn(message: String) {
        console.warn("[WARNING] ${tag()}: $message")
    }

    actual fun error(message: String) {
        console.error("[ERROR] ${tag()}: $message")
    }

    actual fun error(message: String, exception: Throwable) {
        console.error("[ERROR] ${tag()}: $message")
        console.error(exception.message)
    }

    actual fun tag(): String {
        val stack = (js("new Error().stack") as String).lines()
        val caller = stack.getOrNull(3) ?: "unknown"
        return caller
    }

}