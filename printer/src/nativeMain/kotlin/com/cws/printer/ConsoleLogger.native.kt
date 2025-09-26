package com.cws.printer

import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.fflush
import platform.posix.fprintf
import platform.posix.stderr
import platform.posix.stdout
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalForeignApi::class)
actual class ConsoleLogger actual constructor() {

    actual fun v(message: String) {
        val tag = tag()
        fprintf(stdout, "[VERBOSE] ${tag.className}.${tag.methodName}: $message")
        fflush(stdout)
    }

    actual fun i(message: String) {
        val tag = tag()
        fprintf(stdout, "[INFO] ${tag.className}.${tag.methodName}: $message")
        fflush(stdout)
    }

    actual fun d(message: String) {
        val tag = tag()
        fprintf(stdout, "[DEBUG] ${tag.className}.${tag.methodName}: $message")
        fflush(stdout)
    }

    actual fun w(message: String) {
        val tag = tag()
        fprintf(stdout, "[WARNING] ${tag.className}.${tag.methodName}: $message")
        fflush(stdout)
    }

    actual fun e(message: String) {
        val tag = tag()
        fprintf(stderr,"[ERROR] ${tag.className}.${tag.methodName}: $message")
    }

    actual fun e(message: String, exception: Throwable) {
        val tag = tag()
        fprintf(stderr,"[ERROR] ${tag.className}.${tag.methodName}: $message")
        exception.printStackTrace()
    }

    @OptIn(ExperimentalNativeApi::class)
    actual fun tag(): Tag {
        val stack = Throwable().getStackTrace()
        val className = stack.getOrNull(3) ?: return Tag("", "")
        val methodName = stack.getOrNull(4) ?: return Tag(className, "")
        return Tag(className, methodName)
    }

}