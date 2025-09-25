package com.cws.kanvas.loaders

import com.cws.kanvas.texture.Texture

interface TextureLoader {
    suspend fun load(relativePath: String): Texture
}