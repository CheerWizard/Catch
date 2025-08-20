package com.cws.acatch.graphics

import android.content.Context
import android.opengl.GLES30.*
import timber.log.Timber

class Shader(
    private val stages: MutableList<ShaderStage> = mutableListOf()
) {

    private var handle = HANDLE_NULL
    private val linkStatus = IntArray(1)

    fun init(
        context: Context,
        paths: List<String>
    ) {
        val sources = paths.map { path ->
            val shaderType = path.toShaderType()
            if (shaderType == HANDLE_NULL) return@map shaderType to ""
            shaderType to context.assets.open(path).use {
                it.readBytes().decodeToString()
            }
        }
        init(sources)
    }

    private fun String.toShaderType(): Int {
        return when {
            contains("_vert") -> GL_VERTEX_SHADER
            contains("_frag") -> GL_FRAGMENT_SHADER
            else -> {
                Timber.e("Unsupported shader type=$this")
                HANDLE_NULL
            }
        }
    }

    fun init(sources: List<Pair<Int, String>>) {
        sources.forEachIndexed { i, source ->
            val stage = ShaderStage()
            stage.init(source.first)
            if (!stage.compile(source.second)) return@forEachIndexed
            stages.add(i, stage)
        }

        stages.forEach {
            it.attach(handle)
        }

        glLinkProgram(handle)
        glGetProgramiv(handle, GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] == 0) {
            Timber.e(RuntimeException("Failed to link shader: ${glGetProgramInfoLog(handle)}"))
            release()
        }

        stages.forEach {
            it.detach(handle)
            it.release()
        }
    }

    fun release() {
        if (handle == HANDLE_NULL) {
            handle = HANDLE_NULL
            glDeleteProgram(handle)
        }
        stages.forEach {
            it.release()
        }
    }

    fun run() {
        if (handle != HANDLE_NULL) {
            glUseProgram(handle)
        }
    }

}