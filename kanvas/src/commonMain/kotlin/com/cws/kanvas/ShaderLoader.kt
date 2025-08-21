package com.cws.kanvas

expect class ShaderLoader {
    suspend fun load(filepath: String): String
}