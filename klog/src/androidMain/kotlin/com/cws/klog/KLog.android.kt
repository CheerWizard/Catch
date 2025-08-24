package com.cws.klog

import android.util.Log

actual object KLog {

    actual var enabled: Boolean = BuildConfig.DEBUG

    actual fun verbose(message: String) {
        if (enabled) {
            Log.v(tag(), message)
        }
    }

    actual fun info(message: String) {
        if (enabled) {
            Log.i(tag(), message)
        }
    }

    actual fun debug(message: String) {
        if (enabled) {
            Log.d(tag(), message)
        }
    }

    actual fun warn(message: String) {
        if (enabled) {
            Log.w(tag(), message)
        }
    }

    actual fun error(message: String) {
        if (enabled) {
            Log.e(tag(), message)
        }
    }

    actual fun error(message: String, exception: Throwable) {
        if (enabled) {
            Log.e(tag(), message, exception)
        }
    }

    actual fun tag(): String {
        val stack = Throwable().stackTrace
        val caller = stack.getOrNull(3)
        val className = caller?.className?.substringAfterLast('.') ?: "Unknown"
        val methodName = caller?.methodName ?: "unknown"
        return "$className.$methodName()"
    }

}