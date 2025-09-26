package com.cws.printer

actual class ConsoleLogger actual constructor() {

    actual fun v(message: String) {
        console.info("[VERBOSE] ${tag().className}: $message")
    }

    actual fun i(message: String) {
        console.info("[INFO] ${tag().className}: $message")
    }

    actual fun d(message: String) {
        console.info("[DEBUG] ${tag().className}: $message")
    }

    actual fun w(message: String) {
        console.warn("[WARNING] ${tag().className}: $message")
    }

    actual fun e(message: String) {
        console.error("[ERROR] ${tag().className}: $message")
    }

    actual fun e(message: String, exception: Throwable) {
        console.error("[ERROR] ${tag().className}: $message")
        console.error(exception.message)
    }

    actual fun tag(): Tag {
        val stack = (js("new Error().stack") as String).lines()
        val caller = stack.getOrNull(2) ?: ""
        return Tag(caller, "")
    }

}