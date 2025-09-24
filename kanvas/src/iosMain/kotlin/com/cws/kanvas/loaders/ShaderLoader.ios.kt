package com.cws.kanvas.loaders

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

@OptIn(ExperimentalForeignApi::class)
class ShaderLoaderImpl : ShaderLoader {

    override suspend fun load(name: String): String {
        val bundle = NSBundle.mainBundle
        val filepath = "shaders/gles3/$name"
        val path = bundle.pathForResource(filepath, ofType = null)
            ?: error("Failed to find shader $filepath")
        return NSString.stringWithContentsOfFile(path, NSUTF8StringEncoding, null).orEmpty()
    }

}