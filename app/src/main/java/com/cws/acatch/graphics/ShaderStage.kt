package com.cws.acatch.graphics

import android.opengl.GLES30.*
import timber.log.Timber

class ShaderStage {

    private var handle = GL_NULL
    private val status = IntArray(1)

    fun init(type: Int) {
        if (handle == GL_NULL) {
            handle = glCreateShader(type)
        }
    }

    fun release() {
        if (handle != GL_NULL) {
            handle = GL_NULL
            glDeleteShader(handle)
        }
    }

    fun compile(source: String): Boolean {
        if (handle == GL_NULL) {
            Timber.e(IllegalArgumentException("Shader is not created!"))
            return false
        }

        glShaderSource(handle, source)
        glCompileShader(handle)
        glGetShaderiv(handle, GL_COMPILE_STATUS, status, 0)

        if (status[0] == 0) {
            val log = glGetShaderInfoLog(handle)
            release()
            Timber.e(RuntimeException("Failed to compile shader: $log"))
            return false
        }

        return true
    }

    fun attach(program: Int) {
        glAttachShader(program, handle)
    }

    fun detach(program: Int) {
        glDetachShader(program, handle)
    }

}