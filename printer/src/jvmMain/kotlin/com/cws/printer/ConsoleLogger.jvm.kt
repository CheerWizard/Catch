package com.cws.printer

actual class ConsoleLogger actual constructor() {

    actual fun log(logLevel: LogLevel, tag: String, message: String, exception: Throwable?) {
        val formattedMessage = formatLog(logLevel, tag, message)

        if (logLevel.ordinal >= LogLevel.ERROR.ordinal) {
            System.err.println(formattedMessage)
        } else {
            println(formattedMessage)
        }

        exception?.printStackTrace()
    }

}