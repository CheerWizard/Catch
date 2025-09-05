package com.cws.kanvas

import android.opengl.GLES30.*
import java.nio.Buffer
import java.nio.ByteBuffer

actual typealias VertexArrayID = IntArray
actual typealias BufferID = IntArray
actual typealias FrameBufferID = IntArray
actual typealias TextureID = IntArray
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
        glClear(bitmask)
    }

    actual fun clearColor(r: Float, g: Float, b: Float, a: Float) {
        glClearColor(r, g, b, a)
    }

    actual fun viewport(x: Int, y: Int, w: Int, h: Int) {
        glViewport(x, y, w, h)
    }

    actual fun bufferInit(): BufferID {
        val buffer = IntArray(1)
        glGenBuffers(1, buffer, 0)
        return buffer
    }

    actual fun bufferRelease(buffer: BufferID) {
        glDeleteBuffers(1, buffer, 0)
    }

    actual fun bufferBind(type: Int, buffer: BufferID) {
        glBindBuffer(type, buffer[0])
    }

    actual fun bufferBindLocation(type: Int, buffer: BufferID, location: Int) {
        glBindBufferBase(type, location, buffer[0])
    }

    actual fun bufferData(
        type: Int,
        offset: Int,
        data: Any,
        size: Int,
        usage: Int
    ) {
        data as Buffer
        if (data.remaining() < size) {
            glBufferData(type, size, null, usage)
        } else {
            glBufferData(type, size, data, usage)
        }
    }

    actual fun bufferSubData(
        type: Int,
        offset: Int,
        data: Any,
        size: Int
    ) {
        data as Buffer
        if (data.remaining() < size) {
            glBufferSubData(type, offset, size, null)
        } else {
            glBufferSubData(type, offset, size, data)
        }
    }

    actual fun vertexArrayInit(): VertexArrayID {
        val vertexArray = IntArray(1)
        glGenVertexArrays(1, vertexArray, 0)
        return vertexArray
    }

    actual fun vertexArrayRelease(vertexArray: VertexArrayID) {
        glDeleteVertexArrays(1, vertexArray, 0)
    }

    actual fun vertexArrayBind(vertexArray: VertexArrayID) {
        glBindVertexArray(vertexArray[0])
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
                attributeOffset
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
        glGetShaderiv(shaderStage, COMPILE_STATUS, compileStatus, 0)

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
        glGetProgramiv(shader, LINK_STATUS, linkStatus, 0)
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

    actual fun textureInit(type: Int): TextureID {
        val textureID = intArrayOf(1)
        glGenTextures(1, textureID, 0)
        return textureID
    }

    actual fun textureParameter(type: Int, name: Int, value: Int) {
        glTexParameteri(type, name, value)
    }

    actual fun textureRelease(texture: TextureID) {
        glDeleteTextures(1, texture, 0)
    }

    actual fun textureBind(type: Int, texture: TextureID) {
        glBindTexture(type, texture[0])
    }

    actual fun textureUnbind(type: Int) {
        glBindTexture(type, 0)
    }

    actual fun textureActive(slot: Int) {
        glActiveTexture(GL_TEXTURE0 + slot)
    }

    actual fun textureGenerateMipmap(type: Int) {
        glGenerateMipmap(type)
    }

    actual fun textureImage2D(type: Int, texture: Texture) {
        glTexImage2D(
            type,
            texture.mipLevel,
            texture.format,
            texture.width,
            texture.height,
            texture.border,
            texture.format,
            texture.pixelFormat,
            texture.pixels as ByteBuffer
        )
    }

    actual fun drawArrays(mode: Int, first: Int, count: Int) {
        glDrawArrays(mode, first, count)
    }

    actual fun drawArraysInstanced(mode: Int, first: Int, count: Int, instances: Int) {
        glDrawArraysInstanced(mode, first, count, instances)
    }

    actual fun drawElements(mode: Int, indices: Int, type: Int, indicesOffset: Int) {
        glDrawElements(mode, indices, type, indicesOffset)
    }

    actual fun drawElementsInstanced(mode: Int, indices: Int, type: Int, indicesOffset: Int, instances: Int) {
        glDrawElementsInstanced(mode, indices, type, indicesOffset, instances)
    }

    actual fun frameBufferInit(): FrameBufferID {
        val frameBufferID = intArrayOf(1)
        glGenFramebuffers(1, frameBufferID, 0)
        return frameBufferID
    }

    actual fun frameBufferRelease(frameBufferID: FrameBufferID) {
        glDeleteFramebuffers(1, frameBufferID, 0)
    }

    actual fun frameBufferBind(type: Int, frameBufferID: FrameBufferID) {
        glBindFramebuffer(type, frameBufferID[0])
    }

    actual fun frameBufferUnbind(type: Int) {
        glBindFramebuffer(type, 0)
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
            bitmask, filter
        )
    }

    actual fun frameBufferCheckStatus(): Boolean {
        return glCheckFramebufferStatus(FRAME_BUFFER) == GL_FRAMEBUFFER_COMPLETE
    }

    actual fun frameBufferAttachColor(
        index: Int,
        textureType: Int,
        textureID: TextureID,
        textureLevel: Int
    ) {
        glFramebufferTexture2D(
            FRAME_BUFFER,
            GL_COLOR_ATTACHMENT0 + index,
            textureType,
            textureID[0],
            textureLevel
        )
    }

    actual fun frameBufferAttachDepth(
        textureType: Int,
        textureID: TextureID,
        textureLevel: Int
    ) {
        glFramebufferTexture2D(
            FRAME_BUFFER,
            GL_DEPTH_ATTACHMENT,
            textureType,
            textureID[0],
            textureLevel
        )
    }

}