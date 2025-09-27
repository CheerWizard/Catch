package com.cws.printer

expect class CrashlyticsLogger {
    fun log(message: String, throwable: Throwable? = null)
}