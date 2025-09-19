package com.cws.kanvas

class ShaderLoaderImpl : ShaderLoader {

    override suspend fun load(name: String): String {
        val filepath = "/shaders/gl/$name"
        val stream = this::class.java.getResourceAsStream(filepath)
            ?: error("Failed to find shader $filepath")
        return stream.bufferedReader().use { it.readText() }
    }

}