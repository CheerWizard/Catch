package com.cws.printer

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import platform.posix.FILE
import platform.posix.fclose
import platform.posix.fflush
import platform.posix.fopen
import platform.posix.fwrite

@OptIn(ExperimentalForeignApi::class)
actual class FileLogger() {

    private var file: CPointer<FILE>? = null

    actual fun open(name: String, filepath: String) {
        file = fopen(filepath, "w")
    }

    actual fun close() {
        if (file != null) {
            fclose(file)
        }
        file = null
    }

    actual fun log(message: String) {
        if (file != null) {
            memScoped {
                // TODO: can be optimized by caching messages in nativeHeap, to remove need of memScoped arena
                fwrite(message.cstr.getPointer(memScope), 1u, message.length.toULong(), file)
                fflush(file)
            }
        }
    }

}