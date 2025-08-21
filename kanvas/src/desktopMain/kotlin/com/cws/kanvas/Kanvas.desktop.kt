package com.cws.kanvas

import com.cws.kmemory.math.Color
import org.lwjgl.opengl.GL46.*

import java.nio.ByteBuffer

actual typealias VertexArrayID = Int
actual typealias BufferID = Int
actual typealias TextureID = Int
actual typealias ShaderStageID = Int
actual typealias ShaderID = Int

actual object Kanvas {

    actual val NULL: Int = -1
    actual val STATIC_DRAW: Int = GL_STATIC_DRAW
    actual val DYNAMIC_DRAW: Int = GL_DYNAMIC_DRAW
    actual val FLOAT: Int = GL_FLOAT
    actual val INT: Int = GL_INT
    actual val UINT: Int = GL_UNSIGNED_INT
    actual val BOOLEAN: Int = GL_BOOL
    actual val VERTEX_BUFFER: Int = GL_ARRAY_BUFFER
    actual val INDEX_BUFFER: Int = GL_ELEMENT_ARRAY_BUFFER
    actual val UNIFORM_BUFFER: Int = GL_UNIFORM_BUFFER
    actual val FRAME_BUFFER: Int = GL_FRAMEBUFFER
    actual val VERTEX_SHADER: Int = GL_VERTEX_SHADER
    actual val FRAGMENT_SHADER: Int = GL_FRAGMENT_SHADER
    actual val GEOMETRY_SHADER: Int = GL_GEOMETRY_SHADER
    actual val TESS_CONTROL_SHADER: Int = GL_TESS_CONTROL_SHADER
    actual val TESS_EVAL_SHADER: Int = GL_TESS_EVALUATION_SHADER
    actual val COMPUTE_SHADER: Int = GL_COMPUTE_SHADER
    actual val COMPILE_STATUS: Int = GL_COMPILE_STATUS
    actual val LINK_STATUS: Int = GL_LINK_STATUS
    actual val COLOR_BUFFER_BIT: Int = GL_COLOR_BUFFER_BIT
    actual val DEPTH_BUFFER_BIT: Int = GL_DEPTH_BUFFER_BIT
    actual val STENCIL_BUFFER_BIT: Int = GL_STENCIL_BUFFER_BIT

    actual fun clear(bitmask: Int) {
        glClear(bitmask)
    }

    actual fun clearColor(color: Color) {
        val vec4 = color.toVec4()
        glClearColor(vec4.x, vec4.y, vec4.z, vec4.w)
    }

    actual fun viewport(x: Int, y: Int, w: Int, h: Int) {
        glViewport(x, y, w, h)
    }

    actual fun bufferInit(): BufferID {
        return glGenBuffers()
    }

    actual fun bufferRelease(buffer: BufferID) {
        glDeleteBuffers(buffer)
    }

    actual fun bufferBind(type: Int, buffer: BufferID) {
        glBindBuffer(type, buffer)
    }

    actual fun bufferBindLocation(type: Int, buffer: BufferID, location: Int) {
        glBindBufferBase(type, location, buffer)
    }

    actual fun bufferData(
        type: Int,
        offset: Int,
        data: Any?,
        size: Int,
        usage: Int
    ) {
        glBufferData(type, data as ByteBuffer, usage)
    }

    actual fun bufferSubData(
        type: Int,
        offset: Int,
        data: Any?,
        size: Int
    ) {
        glBufferSubData(type, offset.toLong(), data as ByteBuffer)
    }

    actual fun vertexArrayInit(): VertexArrayID {
        return glGenVertexArrays()
    }

    actual fun vertexArrayRelease(vertexArray: VertexArrayID) {
        glDeleteVertexArrays(vertexArray)
    }

    actual fun vertexArrayBind(vertexArray: VertexArrayID) {
        glBindVertexArray(vertexArray)
    }

    actual fun vertexArrayEnableAttributes(attributes: List<VertexAttribute>) {
        var attributeOffset = 0
        attributes.forEach { attribute ->
            glEnableVertexAttribArray(attribute.location)
            glVertexAttribPointer(
                attribute.location,
                attribute.type.size,
                attribute.type.type,
                false,
                attribute.type.stride,
                attributeOffset.toLong()
            )
            glVertexAttribDivisor(
                attribute.location,
                if (attribute.enableInstancing) 1 else 0
            )
            attributeOffset += attribute.type.stride
        }
    }

    actual fun vertexArrayDisableAttributes(attributes: List<VertexAttribute>) {
        attributes.forEach { attribute ->
            glDisableVertexAttribArray(attribute.location)
        }
    }

    actual fun shaderStageInit(type: Int): ShaderStageID {
        return glCreateShader(type)
    }

    actual fun shaderStageRelease(shaderStage: ShaderStageID) {
        glDeleteShader(shaderStage)
    }

    private val compileStatus = IntArray(1)

    actual fun shaderStageCompile(shaderStage: ShaderStageID, source: String): Boolean {
        if (shaderStage == NULL) {
            IllegalArgumentException("Shader is not created!").printStackTrace()
            return false
        }

        glShaderSource(shaderStage, source)
        glCompileShader(shaderStage)
        glGetShaderiv(shaderStage, GL_COMPILE_STATUS, compileStatus)

        if (compileStatus[0] == 0) {
            val log = glGetShaderInfoLog(shaderStage)
            shaderStageRelease(shaderStage)
            RuntimeException("Failed to compile shader: $log").printStackTrace()
            return false
        }

        return true
    }

    actual fun shaderStageAttach(shader: ShaderID, shaderStage: ShaderStageID) {
        glAttachShader(shader, shaderStage)
    }

    actual fun shaderStageDetach(shader: ShaderID, shaderStage: ShaderStageID) {
        glDetachShader(shader, shaderStage)
    }

    actual fun shaderInit(): ShaderID {
        return glCreateProgram()
    }

    actual fun shaderRelease(shader: ShaderID) {
        glDeleteProgram(shader)
    }

    private val linkStatus = IntArray(1)

    actual fun shaderLink(shader: ShaderID): Boolean {
        glLinkProgram(shader)
        glGetProgramiv(shader, GL_LINK_STATUS, linkStatus)
        if (linkStatus[0] == 0) {
            RuntimeException("Failed to link shader: ${glGetProgramInfoLog(shader)}").printStackTrace()
            shaderRelease(shader)
            return false
        }
        return true
    }

    actual fun shaderUse(shader: ShaderID) {
        glUseProgram(shader)
    }

}