package com.cws.kanvas.core

import com.cws.kanvas.texture.Texture
import com.cws.kanvas.pipeline.VertexAttribute
import com.cws.klog.KLog
import com.cws.fmm.BigBuffer
import org.lwjgl.opengl.GL46.*

actual typealias VertexArrayID = Int
actual typealias BufferID = Int
actual typealias TextureID = Int
actual typealias FrameBufferID = Int
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
    actual const val GEOMETRY_SHADER: Int = GL_GEOMETRY_SHADER
    actual const val TESS_CONTROL_SHADER: Int = GL_TESS_CONTROL_SHADER
    actual const val TESS_EVAL_SHADER: Int = GL_TESS_EVALUATION_SHADER
    actual const val COMPUTE_SHADER: Int = GL_COMPUTE_SHADER
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
        return glGenBuffers()
    }

    actual fun bufferRelease(id: BufferID) {
        glDeleteBuffers(id)
    }

    actual fun bufferBind(type: Int, id: BufferID) {
        glBindBuffer(type, id)
    }

    actual fun bufferBindLocation(type: Int, id: BufferID, location: Int) {
        glBindBufferBase(type, location, id)
    }

    actual fun bufferData(
        type: Int,
        offset: Int,
        data: BigBuffer,
        size: Int,
        usage: Int
    ) {
        glBufferData(type, data.buffer, usage)
    }

    actual fun bufferSubData(
        type: Int,
        offset: Int,
        data: BigBuffer,
        size: Int
    ) {
        glBufferSubData(type, offset.toLong(), data.buffer)
    }

    actual fun vertexArrayInit(): VertexArrayID {
        return glGenVertexArrays()
    }

    actual fun vertexArrayRelease(id: VertexArrayID) {
        glDeleteVertexArrays(id)
    }

    actual fun vertexArrayBind(id: VertexArrayID) {
        glBindVertexArray(id)
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
        glGetShaderiv(id, GL_COMPILE_STATUS, compileStatus)

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
        glGetProgramiv(id, GL_LINK_STATUS, linkStatus)
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
        return glGenTextures()
    }

    actual fun textureParameter(type: Int, name: Int, value: Int) {
        glTexParameteri(type, name, value)
    }

    actual fun textureRelease(id: TextureID) {
        glDeleteTextures(id)
    }

    actual fun textureBind(type: Int, id: TextureID) {
        glBindTexture(type, id)
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
        glDrawElements(mode, indices, type, indicesOffset.toLong())
    }

    actual fun drawElementsInstanced(mode: Int, indices: Int, type: Int, indicesOffset: Int, instances: Int) {
        glDrawElementsInstanced(mode, indices, type, indicesOffset.toLong(), instances)
    }

    actual fun frameBufferInit(): FrameBufferID {
        return glGenFramebuffers()
    }

    actual fun frameBufferRelease(id: FrameBufferID) {
        glDeleteFramebuffers(id)
    }

    actual fun frameBufferBind(type: Int, id: FrameBufferID) {
        glBindFramebuffer(type, id)
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
            id,
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
            id,
            textureLevel
        )
    }

}