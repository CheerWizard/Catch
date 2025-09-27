package com.cws.printer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object Printer {

    var logLevel: LogLevel = LogLevel.NONE

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val mutex = Mutex()
    private val consoleLogger = ConsoleLogger()
    private var fileLogger: FileLogger? = null
    private var firebaseLogger: FirebaseLogger? = null

    fun init(
        name: String = "Printer",
        logLevel: LogLevel = LogLevel.NONE,
        fileLogger: FileLogger? = null,
        firebaseLogger: FirebaseLogger? = null
    ) {
        launch {
            this.logLevel = logLevel

            this.fileLogger = fileLogger
            this.fileLogger?.open(name, "logs/${name}_${getCurrentTimestamp()}.logs")

            this.firebaseLogger = firebaseLogger
            this.firebaseLogger?.open()
        }
    }

    // optional to call, not really required to call from client side
    fun close() {
        launch {
            fileLogger?.close()
            firebaseLogger?.close()
        }
    }

    fun v(tag: String, message: String) = log(LogLevel.VERBOSE, tag, message)
    fun i(tag: String, message: String) = log(LogLevel.INFO, tag, message)
    fun d(tag: String, message: String) = log(LogLevel.DEBUG, tag, message)
    fun w(tag: String, message: String) = log(LogLevel.WARNING, tag, message)
    fun e(tag: String, message: String) = log(LogLevel.ERROR, tag, message)
    fun e(tag: String, message: String, exception: Throwable) = log(LogLevel.FATAL, tag, message, exception)

    private fun log(logLevel: LogLevel, tag: String, message: String, exception: Throwable? = null) {
        if (this.logLevel <= logLevel && this.logLevel != LogLevel.NONE) {
            launch {
                consoleLogger.log(logLevel, tag, message, exception)
                fileLogger?.log(formatLog(logLevel, tag, message, exception))
                firebaseLogger?.log(logLevel, tag, message, exception)
            }
        }
    }

    private inline fun launch(crossinline block: () -> Unit) {
        scope.launch {
            mutex.withLock {
                block()
            }
        }
    }

}

