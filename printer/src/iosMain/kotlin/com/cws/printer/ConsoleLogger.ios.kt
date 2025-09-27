package com.cws.printer

import platform.Foundation.NSLog

actual class ConsoleLogger actual constructor() {

    actual fun log(logLevel: LogLevel, tag: String, message: String, exception: Throwable?) {
        val formattedMessage = "$logLevel $tag: $message"
        NSLog(formattedMessage)
        exception?.printStackTrace()
    }

}