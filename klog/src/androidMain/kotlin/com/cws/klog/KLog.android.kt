package com.cws.klog

import android.util.Log

actual object KLog {

    actual var enabled: Boolean = BuildConfig.DEBUG

    actual fun verbose(message: String) {
        if (enabled) {
            val tag = tag()
            Log.v(tag.className, "${tag.methodName}: $message")
        }
    }

    actual fun info(message: String) {
        if (enabled) {
            val tag = tag()
            Log.i(tag.className, "${tag.methodName}: $message")
        }
    }

    actual fun debug(message: String) {
        if (enabled) {
            val tag = tag()
            Log.d(tag.className, "${tag.methodName}: $message")
        }
    }

    actual fun warn(message: String) {
        if (enabled) {
            val tag = tag()
            Log.w(tag.className, "${tag.methodName}: $message")
        }
    }

    actual fun error(message: String) {
        if (enabled) {
            val tag = tag()
            Log.e(tag.className, "${tag.methodName}: $message")
        }
    }

    actual fun error(message: String, exception: Throwable) {
        if (enabled) {
            val tag = tag()
            Log.e(tag.className, "${tag.methodName}: $message", exception)
        }
    }

    actual fun tag(): Tag {
        val stack = Throwable().stackTrace
        val caller = stack.getOrNull(3)
        val className = caller?.className?.substringAfterLast('.') ?: ""
        val methodName = caller?.methodName ?: ""
        return Tag(className, methodName)
    }

}