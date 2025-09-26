package com.cws.printer

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.writeToFile

actual class FileLogger() {

    private var filepath: String? = null

    actual fun open(name: String, filepath: String) {
        val paths = NSSearchPathForDirectoriesInDomains(
            directory = NSDocumentDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true
        )
        val internalDocumentsDir = paths.firstOrNull() as? String ?: return
        this.filepath = "$internalDocumentsDir/$filepath"
    }

    actual fun close() {
        filepath = null
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun log(message: String) {
        filepath?.let { path ->
            (message as NSString).writeToFile(
                path = path,
                atomically = true,
                encoding = NSUTF8StringEncoding,
                error = null
            )
        }
    }

}