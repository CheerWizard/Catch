package com.cws.kanvas

interface ShaderLoader {
    suspend fun load(name: String): String
}