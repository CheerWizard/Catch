package com.cws.printer

actual class ConsoleLogger actual constructor() {

    actual fun log(logLevel: LogLevel, tag: String, message: String, exception: Throwable?) {
        val formattedMessage = formatLog(logLevel, tag, message)

        if (logLevel.ordinal >= LogLevel.ERROR.ordinal) {
            console.error(formattedMessage)
        } else if (logLevel.ordinal == LogLevel.WARNING.ordinal) {
            console.warn(formattedMessage)
        } else {
            console.info(formattedMessage)
        }

        exception?.printStackTrace()
    }

}