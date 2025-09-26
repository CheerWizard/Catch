package com.cws.printer

import android.util.Log

actual class ConsoleLogger actual constructor() {

    actual fun v(message: String) {
        val tag = tag()
        Log.v(tag.className, "${tag.methodName}: $message")
    }

    actual fun i(message: String) {
        val tag = tag()
        Log.i(tag.className, "${tag.methodName}: $message")
    }

    actual fun d(message: String) {
        val tag = tag()
        Log.d(tag.className, "${tag.methodName}: $message")
    }

    actual fun w(message: String) {
        val tag = tag()
        Log.w(tag.className, "${tag.methodName}: $message")
    }

    actual fun e(message: String) {
        val tag = tag()
        Log.e(tag.className, "${tag.methodName}: $message")
    }

    actual fun e(message: String, exception: Throwable) {
        val tag = tag()
        Log.e(tag.className, "${tag.methodName}: $message", exception)
    }

    actual fun tag(): Tag {
        val stack = Throwable().stackTrace
        val caller = stack.getOrNull(3)
        val className = caller?.className?.substringAfterLast('.') ?: ""
        val methodName = caller?.methodName ?: ""
        return Tag(className, methodName)
    }

}