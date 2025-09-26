package com.cws.printer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class Tag(
    val className: String,
    val methodName: String
)

object Printer {

    var enable: Boolean = BuildConfig.DEBUG
    var enableNetworkLogging = false

    private val scope = CoroutineScope(Dispatchers.Default)
    private val mutex = Mutex()
    private val consoleLogger = ConsoleLogger()
    private var fileLogger: FileLogger? = null

    private fun getTimestamp(): String {
        return getCurrentTime().formatDateTime("dd.MM.YYYY HH:mm:ss")
    }

    private inline fun launch(crossinline block: () -> Unit) {
        scope.launch {
            mutex.withLock {
                block()
            }
        }
    }

    fun init(
        name: String = "Printer",
        fileLogger: FileLogger? = null
    ) {
        launch {
            this.fileLogger = fileLogger
            this.fileLogger?.open("logs/${name}_${getTimestamp()}.logs")
        }
    }

    fun close() {
        launch {
            fileLogger?.close()
        }
    }

    fun v(message: String = "") {
        launch {
            if (enable) {
                consoleLogger.v(message)
                fileLogger?.log("${getTimestamp()} [VERBOSE]: $message")
            }
        }
    }

    fun i(message: String = "") {
        launch {
            if (enable) {
                consoleLogger.i(message)
                fileLogger?.log("${getTimestamp()} [INFO]: $message")
            }
        }
    }

    fun d(message: String = "") {
        launch {
            if (enable) {
                consoleLogger.d(message)
                fileLogger?.log("${getTimestamp()} [DEBUG]: $message")
            }
        }
    }

    fun w(message: String = "") {
        launch {
            if (enable) {
                consoleLogger.w(message)
                fileLogger?.log("${getTimestamp()} [WARNING]: $message")
            }
        }
    }

    fun e(message: String = "") {
        launch {
            if (enable) {
                consoleLogger.e(message)
                fileLogger?.log("${getTimestamp()} [ERROR]: $message")
            }
        }
    }

    fun e(message: String = "", exception: Throwable) {
        launch {
            if (enable) {
                consoleLogger.e(message, exception)
                fileLogger?.log("${getTimestamp()} [ERROR]: $message")
            }
        }
    }

}

