package com.cws.printer

import java.io.File

actual class FileLogger() {

    private var file: File? = null

    actual fun open(name: String,  filepath: String) {
        file = File(filepath)
    }

    actual fun close() {
        file = null
    }

    actual fun log(message: String) {
        file?.writeText(message)
    }

}