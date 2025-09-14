package com.cws.kanvas

import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.withContext

actual class PlatformShaderLoader {

    actual suspend fun load(name: String): String {
        return withContext(Dispatchers.Default) {
            val response = window.fetch("/shaders/gles3/$name").await()
            response.text().await()
        }
    }

}