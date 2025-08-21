package com.cws.kanvas

import kotlinx.browser.window
import kotlinx.coroutines.await

actual class ShaderLoader {

    actual suspend fun load(filepath: String): String {
        return window.fetch(filepath).await().text().await()
    }

}