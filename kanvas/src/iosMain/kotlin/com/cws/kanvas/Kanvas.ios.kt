package com.cws.kanvas

import com.cws.klog.KLog
import com.cws.kmemory.BigBuffer
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.cValuesOf
import kotlinx.cinterop.cstr
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import platform.gles3.*
import kotlin.native.internal.NativePtr
import kotlin.toUInt

@OptIn(ExperimentalForeignApi::class)
actual typealias VertexArrayID = NativePtr
@OptIn(ExperimentalForeignApi::class)
actual typealias BufferID = NativePtr
@OptIn(ExperimentalForeignApi::class)
actual typealias TextureID = NativePtr
@OptIn(ExperimentalForeignApi::class)
actual typealias FrameBufferID = NativePtr
actual typealias ShaderStageID = Int
actual typealias ShaderID = Int

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
    actual val READ_FRAME_BUFFER: Int = GL_READ_FRAMEBUFFER
    actual val DRAW_FRAME_BUFFER: Int = GL_DRAW_FRAMEBUFFER
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
    actual val TEXTURE_MIN_FILTER: Int = GL_TEXTURE_MIN_FILTER
    actual val TEXTURE_MAG_FILTER: Int = GL_TEXTURE_MAG_FILTER
    actual val TEXTURE_WRAP_S: Int = GL_TEXTURE_WRAP_S
    actual val TEXTURE_WRAP_T: Int = GL_TEXTURE_WRAP_T
    actual val TEXTURE_WRAP_R: Int = GL_TEXTURE_WRAP_R

    actual fun clear(bitmask: Int) {
        glClear(bitmask.toUInt())
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
        buffer.rawValue
    }

    actual fun bufferRelease(buffer: BufferID) {
        glDeleteBuffers(1, buffer as CArrayPointer<UIntVar>)
    }

    actual fun bufferBind(type: Int, buffer: BufferID) {
        glBindBuffer(type.toUInt(), (buffer as CArrayPointer<UIntVar>)[0])
    }

    actual fun bufferBindLocation(type: Int, buffer: BufferID, location: Int) {
        glBindBufferBase(type.toUInt(), location.toUInt(), (buffer as CArrayPointer<UIntVar>)[0])
    }

    actual fun bufferData(
        type: Int,
        offset: Int,
        data: BigBuffer,
        size: Int,
        usage: Int
    ) {
        glBufferData(type.toUInt(), size.toLong(), data.buffer, usage.toUInt())
    }

    actual fun bufferSubData(
        type: Int,
        offset: Int,
        data: BigBuffer,
        size: Int
    ) {
        glBufferSubData(type.toUInt(), offset.toLong(), size.toLong(), data.buffer)
    }

    actual fun vertexArrayInit(): VertexArrayID = memScoped {
        val vertexArray = allocArray<UIntVar>(1)
        glGenVertexArrays(1, vertexArray)
        vertexArray.rawValue
    }

    actual fun vertexArrayRelease(vertexArray: VertexArrayID) {
        glDeleteVertexArrays(1, vertexArray as CArrayPointer<UIntVar>)
    }

    actual fun vertexArrayBind(vertexArray: VertexArrayID) {
        glBindVertexArray((vertexArray as CArrayPointer<UIntVar>)[0])
    }

    actual fun vertexArrayEnableAttributes(attributes: List<VertexAttribute>) {
        var attributeOffset = 0
        val offset = NativePtr(null)
        attributes.forEach { attribute ->
            glEnableVertexAttribArray(attribute.location.toUInt())
            glVertexAttribPointer(
                attribute.location.toUInt(),
                attribute.type.size,
                attribute.type.type.toUInt(),
                0u,
                attribute.type.stride,
                offset + attributeOffset
            )
            glVertexAttribDivisor(
                attribute.location.toUInt(),
                if (attribute.enableInstancing) 1u else 0u
            )
            attributeOffset += attribute.type.stride
        }
    }

    actual fun vertexArrayDisableAttributes(attributes: List<VertexAttribute>) {
        attributes.forEach { attribute ->
            glDisableVertexAttribArray(attribute.location.toUInt())
        }
    }

    actual fun shaderStageInit(type: Int): ShaderStageID {
        return glCreateShader(type.toUInt()).toInt()
    }

    actual fun shaderStageRelease(shaderStage: ShaderStageID) {
        glDeleteShader(shaderStage.toUInt())
    }

    private val compileStatus = memScoped {
        allocArray<IntVar>(1)
    }

    actual fun shaderStageCompile(shaderStage: ShaderStageID, source: String): Boolean {
        if (shaderStage.toUInt() == NULL.toUInt()) {
            KLog.error("Shader is not created")
            return false
        }

        memScoped {
            val cSource = source.cstr.ptr
            glShaderSource(shaderStage.toUInt(), 1, cValuesOf(cSource), null)
        }

        glCompileShader(shaderStage.toUInt())
        glGetShaderiv(shaderStage.toUInt(), GL_COMPILE_STATUS.toUInt(), compileStatus)

        if (compileStatus[0] == 0) {
            val log = memScoped {
                val logLength = alloc<IntVar>()
                glGetShaderiv(shaderStage.toUInt(), GL_INFO_LOG_LENGTH.toUInt(), logLength.ptr)
                if (logLength.value <= 0) return ""
                val logBuffer = allocArray<ByteVar>(logLength.value)

                glGetShaderInfoLog(shaderStage.toUInt(), logLength.value, null, logBuffer)
                logBuffer.toKString()
            }
            shaderStageRelease(shaderStage.toUInt())
            KLog.error("Failed to compile shader: $log")
            return false
        }

        return true
    }

    actual fun shaderStageAttach(shader: ShaderID, shaderStage: ShaderStageID) {
        glAttachShader(shader.toUInt(), shaderStage.toUInt())
    }

    actual fun shaderStageDetach(shader: ShaderID, shaderStage: ShaderStageID) {
        glDetachShader(shader.toUInt(), shaderStage.toUInt())
    }

    actual fun shaderInit(): ShaderID {
        return glCreateProgram().toInt()
    }

    actual fun shaderRelease(shader: ShaderID) {
        glDeleteProgram(shader.toUInt())
    }

    private val linkStatus = memScoped {
        allocArray<IntVar>(1)
    }

    actual fun shaderLink(shader: ShaderID): Boolean {
        glLinkProgram(shader.toUInt())
        glGetProgramiv(shader.toUInt(), GL_LINK_STATUS, linkStatus)
        if (linkStatus[0] == 0) {
            val log = glGetProgramInfoLog(shader.toUInt())
            KLog.error("Failed to link shader: $log")
            shaderRelease(shader)
            return false
        }
        return true
    }

    actual fun shaderUse(shader: ShaderID) {
        glUseProgram(shader.toUInt())
    }

    actual fun textureInit(type: Int): TextureID = memScoped {
        val textureID = allocArray<UIntVar>(1)
        glGenTextures(1, textureID, 0)
        return textureID.rawValue
    }

    actual fun textureParameter(type: Int, name: Int, value: Int) {
        glTexParameteri(type.toUInt(), name.toUInt(), value)
    }

    actual fun textureRelease(texture: TextureID) {
        glDeleteTextures(1, texture.toUInt(), 0u)
    }

    actual fun textureBind(type: Int, texture: TextureID) {
        glBindTexture(type.toUInt(), texture)
    }

    actual fun textureUnbind(type: Int) {
        glBindTexture(type.toUInt(), 0u)
    }

    actual fun textureActive(slot: Int) {
        glActiveTexture(GL_TEXTURE0.toUInt() + slot.toUInt())
    }

    actual fun textureGenerateMipmap(type: Int) {
        glGenerateMipmap(type.toUInt())
    }

    actual fun textureImage2D(type: Int, texture: Texture) {
        glTexImage2D(
            type.toUInt(),
            texture.mipLevel,
            texture.format,
            texture.width,
            texture.height,
            texture.border,
            texture.format.toUInt(),
            texture.pixelFormat.toUInt(),
            texture.pixels as CValuesRef<*>
        )
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

    actual fun frameBufferInit(): FrameBufferID {
        memScoped {
            val frameBufferID = allocArray<UIntVar>(1)
            glGenFramebuffers(1, frameBufferID)
            return frameBufferID.rawValue
        }
    }

    actual fun frameBufferRelease(frameBufferID: FrameBufferID) {
        glDeleteFramebuffers(1, frameBufferID)
    }

    actual fun frameBufferBind(type: Int, frameBufferID: FrameBufferID) {
        glBindFramebuffer(type.toUInt(), frameBufferID)
    }

    actual fun frameBufferUnbind(type: Int) {
        glBindFramebuffer(type.toUInt(), 0u)
    }

    actual fun frameBufferBlit(
        srcX: Int,
        srcY: Int,
        srcWidth: Int,
        srcHeight: Int,
        destX: Int,
        destY: Int,
        destWidth: Int,
        destHeight: Int,
        bitmask: Int,
        filter: Int
    ) {
        glBlitFramebuffer(
            srcX, srcY, srcWidth, srcHeight,
            destX, destY, destWidth, destHeight,
            bitmask.toUInt(), filter.toUInt()
        )
    }

    actual fun frameBufferCheckStatus(): Boolean {
        return glCheckFramebufferStatus(FRAME_BUFFER.toUInt()) == GL_FRAMEBUFFER_COMPLETE.toUInt()
    }

    actual fun frameBufferAttachColor(
        index: Int,
        textureType: Int,
        textureID: TextureID,
        textureLevel: Int
    ) {
        glFramebufferTexture2D(
            FRAME_BUFFER.toUInt(),
            GL_COLOR_ATTACHMENT0.toUInt() + index.toUInt(),
            textureType.toUInt(),
            textureID,
            textureLevel
        )
    }

    actual fun frameBufferAttachDepth(
        textureType: Int,
        textureID: TextureID,
        textureLevel: Int
    ) {
        glFramebufferTexture2D(
            FRAME_BUFFER.toUInt(),
            GL_DEPTH_ATTACHMENT.toUInt(),
            textureType.toUInt(),
            textureID,
            textureLevel
        )
    }

}