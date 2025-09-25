package com.cws.kanvas.core

import android.opengl.GLES30.*
import com.cws.kanvas.texture.Texture
import com.cws.kanvas.pipeline.VertexAttribute
import com.cws.klog.KLog
import com.cws.kmemory.BigBuffer

actual typealias VertexArrayID = IntArray
actual typealias BufferID = IntArray
actual typealias FrameBufferID = IntArray
actual typealias TextureID = IntArray
actual typealias ShaderStageID = Int
actual typealias ShaderID = Int

actual object Kanvas {

    actual const val NULL: Int = -1
    actual const val STATIC_DRAW: Int = GL_STATIC_DRAW
    actual const val DYNAMIC_DRAW: Int = GL_DYNAMIC_DRAW
    actual const val FLOAT: Int = GL_FLOAT
    actual const val INT: Int = GL_INT
    actual const val UINT: Int = GL_UNSIGNED_INT
    actual const val BOOLEAN: Int = GL_BOOL
    actual const val UBYTE: Int = GL_UNSIGNED_BYTE
    actual const val TRIANGLES: Int = GL_TRIANGLES
    actual const val VERTEX_BUFFER: Int = GL_ARRAY_BUFFER
    actual const val INDEX_BUFFER: Int = GL_ELEMENT_ARRAY_BUFFER
    actual const val UNIFORM_BUFFER: Int = GL_UNIFORM_BUFFER
    actual const val FRAME_BUFFER: Int = GL_FRAMEBUFFER
    actual const val READ_FRAME_BUFFER: Int = GL_READ_FRAMEBUFFER
    actual const val DRAW_FRAME_BUFFER: Int = GL_DRAW_FRAMEBUFFER
    actual const val VERTEX_SHADER: Int = GL_VERTEX_SHADER
    actual const val FRAGMENT_SHADER: Int = GL_FRAGMENT_SHADER
    actual const val GEOMETRY_SHADER: Int = NULL
    actual const val TESS_CONTROL_SHADER: Int = NULL
    actual const val TESS_EVAL_SHADER: Int = NULL
    actual const val COMPUTE_SHADER: Int = NULL
    actual const val COMPILE_STATUS: Int = GL_COMPILE_STATUS
    actual const val LINK_STATUS: Int = GL_LINK_STATUS
    actual const val COLOR_BUFFER_BIT: Int = GL_COLOR_BUFFER_BIT
    actual const val DEPTH_BUFFER_BIT: Int = GL_DEPTH_BUFFER_BIT
    actual const val STENCIL_BUFFER_BIT: Int = GL_STENCIL_BUFFER_BIT
    actual const val FORMAT_RGBA: Int = GL_RGBA
    actual const val FORMAT_RGB: Int = GL_RGB
    actual const val LINEAR: Int = GL_LINEAR
    actual const val CLAMP_TO_EDGE: Int = GL_CLAMP_TO_EDGE
    actual const val REPEAT: Int = GL_REPEAT
    actual const val TEXTURE_2D: Int = GL_TEXTURE_2D
    actual const val TEXTURE_CUBE_MAP: Int = GL_TEXTURE_CUBE_MAP
    actual const val TEXTURE_MIN_FILTER: Int = GL_TEXTURE_MIN_FILTER
    actual const val TEXTURE_MAG_FILTER: Int = GL_TEXTURE_MAG_FILTER
    actual const val TEXTURE_WRAP_S: Int = GL_TEXTURE_WRAP_S
    actual const val TEXTURE_WRAP_T: Int = GL_TEXTURE_WRAP_T
    actual const val TEXTURE_WRAP_R: Int = GL_TEXTURE_WRAP_R

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

    actual fun bufferRelease(id: BufferID) {
        glDeleteBuffers(1, id, 0)
    }

    actual fun bufferBind(type: Int, id: BufferID) {
        glBindBuffer(type, id[0])
    }

    actual fun bufferBindLocation(type: Int, id: BufferID, location: Int) {
        glBindBufferBase(type, location, id[0])
    }

    actual fun bufferData(
        type: Int,
        offset: Int,
        data: BigBuffer,
        size: Int,
        usage: Int
    ) {
        if (data.buffer.remaining() < size) {
            glBufferData(type, size, null, usage)
        } else {
            glBufferData(type, size, data.buffer, usage)
        }
    }

    actual fun bufferSubData(
        type: Int,
        offset: Int,
        data: BigBuffer,
        size: Int
    ) {
        if (data.buffer.remaining() < size) {
            glBufferSubData(type, offset, size, null)
        } else {
            glBufferSubData(type, offset, size, data.buffer)
        }
    }

    actual fun vertexArrayInit(): VertexArrayID {
        val vertexArray = IntArray(1)
        glGenVertexArrays(1, vertexArray, 0)
        return vertexArray
    }

    actual fun vertexArrayRelease(id: VertexArrayID) {
        glDeleteVertexArrays(1, id, 0)
    }

    actual fun vertexArrayBind(id: VertexArrayID) {
        glBindVertexArray(id[0])
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

    actual fun shaderStageRelease(id: ShaderStageID) {
        glDeleteShader(id)
    }

    private val compileStatus = IntArray(1)

    actual fun shaderStageCompile(id: ShaderStageID, source: String): Boolean {
        if (id == NULL) {
            KLog.error("Shader is not created!")
            return false
        }

        glShaderSource(id, source)
        glCompileShader(id)
        glGetShaderiv(id, COMPILE_STATUS, compileStatus, 0)

        if (compileStatus[0] == 0) {
            val log = glGetShaderInfoLog(id)
            shaderStageRelease(id)
            KLog.error("Failed to compile shader: $log")
            return false
        }

        return true
    }

    actual fun shaderStageAttach(id: ShaderID, shaderStage: ShaderStageID) {
        glAttachShader(id, shaderStage)
    }

    actual fun shaderStageDetach(id: ShaderID, shaderStage: ShaderStageID) {
        glDetachShader(id, shaderStage)
    }

    actual fun shaderInit(): ShaderID {
        return glCreateProgram()
    }

    actual fun shaderRelease(id: ShaderID) {
        glDeleteProgram(id)
    }

    private val linkStatus = IntArray(1)

    actual fun shaderLink(id: ShaderID): Boolean {
        glLinkProgram(id)
        glGetProgramiv(id, LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] == 0) {
            KLog.error("Failed to link shader: ${glGetProgramInfoLog(id)}")
            shaderRelease(id)
            return false
        }
        return true
    }

    actual fun shaderUse(id: ShaderID) {
        glUseProgram(id)
    }

    actual fun textureInit(type: Int): TextureID {
        val textureID = intArrayOf(1)
        glGenTextures(1, textureID, 0)
        return textureID
    }

    actual fun textureParameter(type: Int, name: Int, value: Int) {
        glTexParameteri(type, name, value)
    }

    actual fun textureRelease(id: TextureID) {
        glDeleteTextures(1, id, 0)
    }

    actual fun textureBind(type: Int, id: TextureID) {
        glBindTexture(type, id[0])
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
            texture.pixels.buffer
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

    actual fun frameBufferRelease(id: FrameBufferID) {
        glDeleteFramebuffers(1, id, 0)
    }

    actual fun frameBufferBind(type: Int, id: FrameBufferID) {
        glBindFramebuffer(type, id[0])
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
        id: TextureID,
        textureLevel: Int
    ) {
        glFramebufferTexture2D(
            FRAME_BUFFER,
            GL_COLOR_ATTACHMENT0 + index,
            textureType,
            id[0],
            textureLevel
        )
    }

    actual fun frameBufferAttachDepth(
        textureType: Int,
        id: TextureID,
        textureLevel: Int
    ) {
        glFramebufferTexture2D(
            FRAME_BUFFER,
            GL_DEPTH_ATTACHMENT,
            textureType,
            id[0],
            textureLevel
        )
    }

}