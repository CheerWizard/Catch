package com.cws.kanvas

import com.cws.klog.KLog
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread
import platform.posix.ftell

@OptIn(ExperimentalForeignApi::class)
actual class ShaderLoader {

    actual suspend fun load(filepath: String): String {
        val file = fopen(filepath, "rb")
        if (file == null) {
            KLog.error("Failed to load $filepath")
            return ""
        }
        val size = ftell(file)
        val source = memScoped {
            val buffer = allocArray<UByteVar>(size)
            fread(buffer, size.toULong(), size.toULong(), file)
            buffer
        }
        fclose(file)
        return source.toString()
    }

}