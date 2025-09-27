package com.cws.printer

import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.fflush
import platform.posix.fprintf
import platform.posix.stderr
import platform.posix.stdout

@OptIn(ExperimentalForeignApi::class)
actual class ConsoleLogger actual constructor() {

    actual fun log(logLevel: LogLevel, tag: String, message: String, exception: Throwable?) {
        val formattedMessage = formatLog(logLevel, tag, message)

        if (logLevel.ordinal >= LogLevel.ERROR.ordinal) {
            fprintf(stderr, formattedMessage)
        } else {
            fprintf(stdout, formattedMessage)
            fflush(stdout)
        }

        exception?.printStackTrace()
    }

}