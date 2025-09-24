package com.cws.kanvas.loaders

interface ShaderLoader {
    suspend fun load(name: String): String
}