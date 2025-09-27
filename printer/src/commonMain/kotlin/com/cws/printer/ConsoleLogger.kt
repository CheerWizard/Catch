package com.cws.printer

expect class ConsoleLogger() {
    fun log(logLevel: LogLevel, tag: String, message: String, exception: Throwable? = null)
}