package com.cws.printer

import android.util.Log

actual class ConsoleLogger actual constructor() {

    actual fun log(logLevel: LogLevel, tag: String, message: String, exception: Throwable?) {
        if (logLevel == LogLevel.NONE) return

        if (exception == null) {
            Log.println(logLevel.toAndroidLogLevel(), tag, message)
        } else {
            Log.wtf(tag, message, exception)
        }
    }

}