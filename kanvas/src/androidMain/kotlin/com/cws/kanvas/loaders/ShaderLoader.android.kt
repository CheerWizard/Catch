package com.cws.kanvas.loaders

import android.content.Context
import android.content.res.AssetManager
import com.cws.printer.Printer

class ShaderLoaderImpl(context: Context) : ShaderLoader {

    private val assetManager: AssetManager = context.assets

    override suspend fun load(name: String): String {
        val filepath = "shaders/gles3/$name"
        val stream = try {
            assetManager.open(filepath)
        } catch (e: Exception) {
            Printer.e("", e)
            error("Failed to find shader $filepath")
        }
        return stream.bufferedReader().use { it.readText() }
    }

}