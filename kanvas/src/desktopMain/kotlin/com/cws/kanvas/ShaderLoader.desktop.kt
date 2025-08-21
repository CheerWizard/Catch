package com.cws.kanvas

import java.io.File

actual class ShaderLoader {

    actual suspend fun load(filepath: String): String {
        return File(filepath).readText()
    }

}