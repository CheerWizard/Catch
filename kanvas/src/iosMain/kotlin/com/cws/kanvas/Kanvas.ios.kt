package com.cws.kanvas

import com.cws.klog.KLog
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.cValuesOf
import kotlinx.cinterop.cstr
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.gles3.*

actual typealias VertexArrayID = Any
actual typealias BufferID = Any
actual typealias TextureID = Any
actual typealias FrameBufferID = Any
actual typealias ShaderStageID = UInt
actual typealias ShaderID = UInt

@OptIn(ExperimentalForeignApi::class)
actual object Kanvas {

    actual val NULL: Int = -1
    actual val STATIC_DRAW: Int = GL_STATIC_DRAW
    actual val DYNAMIC_DRAW: Int = GL_DYNAMIC_DRAW
    actual val FLOAT: Int = GL_FLOAT
    actual val INT: Int = GL_INT
    actual val UINT: Int = GL_UNSIGNED_INT
    actual val BOOLEAN: Int = GL_BOOL
    actual val UBYTE: Int = GL_UNSIGNED_BYTE
    actual val TRIANGLES: Int = GL_TRIANGLES
    actual val VERTEX_BUFFER: Int = GL_ARRAY_BUFFER
    actual val INDEX_BUFFER: Int = GL_ELEMENT_ARRAY_BUFFER
    actual val UNIFORM_BUFFER: Int = GL_UNIFORM_BUFFER
    actual val FRAME_BUFFER: Int = GL_FRAMEBUFFER
    actual val VERTEX_SHADER: Int = GL_VERTEX_SHADER
    actual val FRAGMENT_SHADER: Int = GL_FRAGMENT_SHADER
    actual val GEOMETRY_SHADER: Int = NULL
    actual val TESS_CONTROL_SHADER: Int = NULL
    actual val TESS_EVAL_SHADER: Int = NULL
    actual val COMPUTE_SHADER: Int = NULL
    actual val COMPILE_STATUS: Int = GL_COMPILE_STATUS
    actual val LINK_STATUS: Int = GL_LINK_STATUS
    actual val COLOR_BUFFER_BIT: Int = GL_COLOR_BUFFER_BIT
    actual val DEPTH_BUFFER_BIT: Int = GL_DEPTH_BUFFER_BIT
    actual val STENCIL_BUFFER_BIT: Int = GL_STENCIL_BUFFER_BIT
    actual val FORMAT_RGBA: Int = GL_RGBA
    actual val FORMAT_RGB: Int = GL_RGB
    actual val LINEAR: Int = GL_LINEAR
    actual val CLAMP_TO_EDGE: Int = GL_CLAMP_TO_EDGE
    actual val REPEAT: Int = GL_REPEAT
    actual val TEXTURE_2D: Int = GL_TEXTURE_2D
    actual val TEXTURE_CUBE_MAP: Int = GL_TEXTURE_CUBE_MAP

    actual fun clear(bitmask: Int) {
        glClear(bitmask)
    }

    actual fun clearColor(r: Float, g: Float, b: Float, a: Float) {
        glClearColor(r, g, b, a)
    }

    actual fun viewport(x: Int, y: Int, w: Int, h: Int) {
        glViewport(x, y, w, h)
    }

    actual fun bufferInit(): BufferID = memScoped {
        val buffer = allocArray<UIntVar>(1)
        glGenBuffers(1, buffer)
        buffer[0]
    }

    actual fun bufferRelease(buffer: BufferID) {
        glDeleteBuffers(1, buffer as CArrayPointer<UIntVar>)
    }

    actual fun bufferBind(type: Int, buffer: BufferID) {
        glBindBuffer(type, (buffer as CArrayPointer<UIntVar>)[0])
    }

    actual fun bufferBindLocation(type: Int, buffer: BufferID, location: Int) {
        glBindBufferBase(type, location, (buffer as CArrayPointer<UIntVar>)[0])
    }

    actual fun bufferData(
        type: Int,
        offset: Int,
        data: Any?,
        size: Int,
        usage: Int
    ) {
        glBufferData(type, size.toLong(), data as CArrayPointer<*>, usage)
    }

    actual fun bufferSubData(
        type: Int,
        offset: Int,
        data: Any?,
        size: Int
    ) {
        glBufferSubData(type, offset.toLong(), size.toLong(), data as CArrayPointer<*>)
    }

    actual fun vertexArrayInit(): VertexArrayID = memScoped {
        val vertexArray = allocArray<UIntVar>(1)
        glGenVertexArrays(1, vertexArray)
        vertexArray
    }

    actual fun vertexArrayRelease(vertexArray: VertexArrayID) {
        glDeleteVertexArrays(1, vertexArray as CArrayPointer<UIntVar>)
    }

    actual fun vertexArrayBind(vertexArray: VertexArrayID) {
        glBindVertexArray((vertexArray as CArrayPointer<UIntVar>)[0])
    }

    actual fun vertexArrayEnableAttributes(attributes: List<VertexAttribute>) {
        var attributeOffset = 0
        attributes.forEach { attribute ->
            glEnableVertexAttribArray(attribute.location)
            glVertexAttribPointer(
                attribute.location,
                attribute.type.size,
                attribute.type.type,
                0u,
                attribute.type.stride,
                attributeOffset.toLong()
            )
            glVertexAttribDivisor(
                attribute.location,
                if (attribute.enableInstancing) 1u else 0u
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
        return glCreateShader(type).toInt()
    }

    actual fun shaderStageRelease(shaderStage: ShaderStageID) {
        glDeleteShader(shaderStage)
    }

    private val compileStatus = memScoped {
        allocArray<IntVar>(1)
    }

    actual fun shaderStageCompile(shaderStage: ShaderStageID, source: String): Boolean {
        if (shaderStage == NULL) {
            KLog.error("Shader is not created")
            return false
        }

        memScoped {
            val cSource = source.cstr.ptr
            glShaderSource(shaderStage, 1, cValuesOf(cSource), null)
        }

        glCompileShader(shaderStage)
        glGetShaderiv(shaderStage, GL_COMPILE_STATUS, compileStatus)

        if (compileStatus[0] == 0) {
            val log = memScoped {
                val logLength = alloc<IntVar>()
                glGetShaderiv(shaderStage, GL_INFO_LOG_LENGTH, logLength.ptr)
                if (logLength.value <= 0) return ""
                val logBuffer = allocArray<ByteVar>(logLength.value)
                glGetShaderInfoLog(shaderStage, logLength.value, null, logBuffer)
                logBuffer.toKString()
            }
            shaderStageRelease(shaderStage)
            KLog.error("Failed to compile shader: $log")
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
        return glCreateProgram().toInt()
    }

    actual fun shaderRelease(shader: ShaderID) {
        glDeleteProgram(shader)
    }

    private val linkStatus = memScoped {
        allocArray<IntVar>(1)
    }

    actual fun shaderLink(shader: ShaderID): Boolean {
        glLinkProgram(shader)
        glGetProgramiv(shader, GL_LINK_STATUS, linkStatus)
        if (linkStatus[0] == 0) {
            val log = glGetProgramInfoLog(shader)
            KLog.error("Failed to link shader: $log")
            shaderRelease(shader)
            return false
        }
        return true
    }

    actual fun shaderUse(shader: ShaderID) {
        glUseProgram(shader)
    }

    actual fun textureInit(type: Int, texture: Texture): TextureID = memScoped {
        val textureID = allocArray<UIntVar>(1)
        glGenTextures(1, textureID)
        glBindTexture(type, textureID[0])
        textureParameter(type, GL_TEXTURE_MIN_FILTER, texture.minFilter)
        textureParameter(type, GL_TEXTURE_MAG_FILTER, texture.magFilter)
        textureParameter(type, GL_TEXTURE_WRAP_S, texture.wrapS)
        textureParameter(type, GL_TEXTURE_WRAP_T, texture.wrapT)
        textureParameter(type, GL_TEXTURE_WRAP_R, texture.wrapR)
        glTexImage2D(
            type,
            texture.mipLevel,
            texture.format,
            texture.width,
            texture.height,
            texture.border,
            texture.format,
            texture.pixelFormat,
            texture.pixels as CValuesRef<*>
        )
        glGenerateMipmap(type)
        glBindTexture(type, 0u)
        textureID
    }

    private fun textureParameter(type: Int, name: Int, value: Int) {
        if (value != NULL) {
            glTexParameteri(type, name, value)
        }
    }

    actual fun textureRelease(texture: TextureID) {
        glDeleteTextures(1, texture as CArrayPointer<UIntVar>)
    }

    actual fun textureBind(type: Int, texture: TextureID, slot: Int) {
        glBindTexture(type, (texture as CArrayPointer<UIntVar>)[0])
        glActiveTexture(GL_TEXTURE0 + slot)
    }

    actual fun drawArrays(mode: Int, first: Int, count: Int) {
        glDrawArrays(mode.toUInt(), first, count)
    }

    actual fun drawArraysInstanced(mode: Int, first: Int, count: Int, instances: Int) {
        glDrawArraysInstanced(mode.toUInt(), first, count, instances)
    }

    actual fun drawElements(mode: Int, indices: Int, type: Int, indicesOffset: Int) {
        glDrawElements(mode.toUInt(), indices, type.toUInt(), null)
    }

    actual fun drawElementsInstanced(mode: Int, indices: Int, type: Int, indicesOffset: Int, instances: Int) {
        glDrawElementsInstanced(
            mode.toUInt(),
            indices,
            type.toUInt(),
            null,
            instances
        )
    }

}