package com.cws.kanvas

import kotlinx.browser.window
import kotlinx.coroutines.await

actual class ShaderLoader {

    actual suspend fun load(name: String): String {
        return window.fetch("./shaders/$name").await().text().await()
    }

}