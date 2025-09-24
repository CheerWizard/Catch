package com.cws.kanvas.texture

import com.cws.kanvas.core.Kanvas

data class Texture(
    val width: Int,
    val height: Int,
    val format: Int,
    val pixelFormat: Int,
    val pixels: Pixels,
    val generateMipMaps: Boolean = false,
    val wrapS: Int = Kanvas.NULL,
    val wrapT: Int = Kanvas.NULL,
    val wrapR: Int = Kanvas.NULL,
    val minFilter: Int = Kanvas.NULL,
    val magFilter: Int = Kanvas.NULL,
    val border: Int = 0,
    val mipLevel: Int = 0
)