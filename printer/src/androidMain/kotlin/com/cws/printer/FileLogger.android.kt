package com.cws.printer

import android.content.Context
import java.io.File

actual class FileLogger(context: Context) {

    private var file: File? = null
    private val internalDir: File = context.filesDir

    actual fun open(name: String, filepath: String) {
        file = File(internalDir, filepath)
    }

    actual fun close() {
        file = null
    }

    actual fun log(message: String) {
        file?.writeText(message)
    }

}