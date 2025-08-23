package com.cws.klog

import android.util.Log

actual object KLog {

    actual fun verbose(message: String) {
        Log.v(tag(), message)
    }

    actual fun info(message: String) {
        Log.i(tag(), message)
    }

    actual fun debug(message: String) {
        Log.d(tag(), message)
    }

    actual fun warn(message: String) {
        Log.w(tag(), message)
    }

    actual fun error(message: String) {
        Log.e(tag(), message)
    }

    actual fun error(message: String, exception: Throwable) {
        Log.e(tag(), message, exception)
    }

    actual fun tag(): String {
        val stack = Throwable().stackTrace
        val caller = stack.getOrNull(3)
        val className = caller?.className?.substringAfterLast('.') ?: "Unknown"
        val methodName = caller?.methodName ?: "unknown"
        return "$className.$methodName()"
    }

}