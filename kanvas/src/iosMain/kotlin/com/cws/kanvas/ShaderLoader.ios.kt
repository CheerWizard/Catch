package com.cws.kanvas

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

@OptIn(ExperimentalForeignApi::class)
actual class ShaderLoader {

    actual suspend fun load(name: String): String {
        val bundle = NSBundle.mainBundle
        val path = bundle.pathForResource(name, ofType = null)
            ?: error("Failed to find shader $name")
        return NSString.stringWithContentsOfFile(path, NSUTF8StringEncoding, null).orEmpty()
    }

}