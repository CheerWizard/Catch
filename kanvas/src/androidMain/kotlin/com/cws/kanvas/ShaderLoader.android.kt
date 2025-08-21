package com.cws.kanvas

import android.content.Context
import kotlin.use

actual class ShaderLoader(context: Context) {

    private val assetManager = context.assets

    actual suspend fun load(filepath: String): String {
        return assetManager.open(filepath).use {
            it.readBytes().decodeToString()
        }
    }

}