package com.cws.kanvas

actual class ShaderLoader {

    actual suspend fun load(name: String): String {
        val stream = this::class.java.getResourceAsStream("shaders/$name")
            ?: error("Failed to find shader $name")
        return stream.bufferedReader().use { it.readText() }
    }

}