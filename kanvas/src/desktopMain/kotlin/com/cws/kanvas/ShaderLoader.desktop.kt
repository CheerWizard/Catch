package com.cws.kanvas

import java.io.File

actual class ShaderLoader {

    actual suspend fun load(filepath: String): String {
        val file = File(filepath)
        if (file.exists()) {
            return file.readText()
        } else {

            return ""
        }
    }

}