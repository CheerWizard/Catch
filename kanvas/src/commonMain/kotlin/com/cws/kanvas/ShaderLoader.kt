package com.cws.kanvas

expect class ShaderLoader() {
    suspend fun load(name: String): String
}