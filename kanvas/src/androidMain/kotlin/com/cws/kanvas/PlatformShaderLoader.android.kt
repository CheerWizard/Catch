package com.cws.kanvas

import com.cws.klog.KLog

actual class PlatformShaderLoader {

    actual suspend fun load(name: String): String {
        val filepath = "shaders/gles3/$name"
        val stream = try {
            AssetProvider.assets.open(filepath)
        } catch (e: Exception) {
            KLog.error("", e)
            error("Failed to find shader $filepath")
        }
        return stream.bufferedReader().use { it.readText() }
    }

}