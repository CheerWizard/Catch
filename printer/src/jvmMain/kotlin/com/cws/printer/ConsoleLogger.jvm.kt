package com.cws.printer

import kotlin.experimental.ExperimentalNativeApi

actual class ConsoleLogger actual constructor() {

    actual fun v(message: String) {
        val tag = tag()
        println("[VERBOSE] ${tag.className}.${tag.methodName}: $message")
    }

    actual fun i(message: String) {
        val tag = tag()
        println("[INFO] ${tag.className}.${tag.methodName}: $message")
    }

    actual fun d(message: String) {
        val tag = tag()
        println("[DEBUG] ${tag.className}.${tag.methodName}: $message")
    }

    actual fun w(message: String) {
        val tag = tag()
        println("[WARNING] ${tag.className}.${tag.methodName}: $message")
    }

    actual fun e(message: String) {
        val tag = tag()
        System.err.println("[ERROR] ${tag.className}.${tag.methodName}: $message")
    }

    actual fun e(message: String, exception: Throwable) {
        val tag = tag()
        System.err.println("[ERROR] ${tag.className}.${tag.methodName}: $message")
        exception.printStackTrace()
    }

    @OptIn(ExperimentalNativeApi::class)
    actual fun tag(): Tag {
        val stack = Throwable().stackTrace
        val caller = stack.getOrNull(3) ?: return Tag("", "")
        return Tag(caller.className, caller.methodName)
    }

}