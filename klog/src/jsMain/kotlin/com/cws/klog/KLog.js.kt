package com.cws.klog

actual object KLog {

    actual var enabled: Boolean = BuildConfig.DEBUG

    actual fun verbose(message: String) {
        if (enabled) {
            console.info("[VERBOSE] ${tag().className}: $message")
        }
    }

    actual fun info(message: String) {
        if (enabled) {
            console.info("[INFO] ${tag().className}: $message")
        }
    }

    actual fun debug(message: String) {
        if (enabled) {
            console.info("[DEBUG] ${tag().className}: $message")
        }
    }

    actual fun warn(message: String) {
        if (enabled) {
            console.warn("[WARNING] ${tag().className}: $message")
        }
    }

    actual fun error(message: String) {
        if (enabled) {
            console.error("[ERROR] ${tag().className}: $message")
        }
    }

    actual fun error(message: String, exception: Throwable) {
        if (enabled) {
            console.error("[ERROR] ${tag().className}: $message")
            console.error(exception.message)
        }
    }

    actual fun tag(): Tag {
        val stack = (js("new Error().stack") as String).lines()
        val caller = stack.getOrNull(2) ?: ""
        return Tag(caller, "")
    }

}