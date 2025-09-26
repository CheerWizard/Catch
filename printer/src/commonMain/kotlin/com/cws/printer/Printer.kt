package com.cws.printer

data class Tag(
    val className: String,
    val methodName: String
)

object Printer {

    var enable: Boolean = BuildConfig.DEBUG
    var enableFileLogging = false
    var enableNetworkLogging = false

    fun v(message: String = "") {
        if (enable) {
            ConsoleLogger.v(message)
        }
    }

    fun i(message: String = "") {
        if (enable) {
            ConsoleLogger.i(message)
        }
    }
    fun d(message: String = "") {
        if (enable) {
            ConsoleLogger.d(message)
        }
    }

    fun w(message: String = "") {
        if (enable) {
            ConsoleLogger.w(message)
        }
    }

    fun e(message: String = "") {
        if (enable) {
            ConsoleLogger.e(message)
        }
    }

    fun e(message: String = "", exception: Throwable) {
        if (enable) {
            ConsoleLogger.e(message, exception)
        }
    }

}

