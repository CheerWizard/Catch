package com.cws.printer

enum class LogLevel {
    NONE,
    VERBOSE,
    DEBUG,
    INFO,
    WARNING,
    ERROR,
    FATAL;

    override fun toString(): String {
        return when (this) {
            NONE -> ""
            VERBOSE -> "[VERBOSE]"
            DEBUG -> "[DEBUG]"
            INFO -> "[INFO]"
            WARNING -> "[WARNING]"
            ERROR -> "[ERROR]"
            FATAL -> "[FATAL]"
        }
    }
}