package com.cws.kanvas.core

import com.cws.kanvas.texture.Texture
import com.cws.kanvas.pipeline.VertexAttribute
import com.cws.kmemory.BigBuffer

expect class VertexArrayID
expect class BufferID
expect class TextureID
expect class FrameBufferID
expect class ShaderStageID
expect class ShaderID

expect object Kanvas {

    val NULL: Int

    val STATIC_DRAW: Int
    val DYNAMIC_DRAW: Int

    val FLOAT: Int
    val INT: Int
    val UINT: Int
    val BOOLEAN: Int
    val UBYTE: Int

    val TRIANGLES: Int

    val VERTEX_BUFFER: Int
    val INDEX_BUFFER: Int
    val UNIFORM_BUFFER: Int
    val FRAME_BUFFER: Int
    val READ_FRAME_BUFFER: Int
    val DRAW_FRAME_BUFFER: Int

    val VERTEX_SHADER: Int
    val FRAGMENT_SHADER: Int
    val GEOMETRY_SHADER: Int
    val TESS_CONTROL_SHADER: Int
    val TESS_EVAL_SHADER: Int
    val COMPUTE_SHADER: Int

    val COMPILE_STATUS: Int
    val LINK_STATUS: Int

    val COLOR_BUFFER_BIT: Int
    val DEPTH_BUFFER_BIT: Int
    val STENCIL_BUFFER_BIT: Int

    val FORMAT_RGBA: Int
    val FORMAT_RGB: Int

    val LINEAR: Int
    val CLAMP_TO_EDGE: Int
    val REPEAT: Int

    val TEXTURE_2D: Int
    val TEXTURE_CUBE_MAP: Int
    val TEXTURE_MIN_FILTER: Int
    val TEXTURE_MAG_FILTER: Int
    val TEXTURE_WRAP_S: Int
    val TEXTURE_WRAP_T: Int
    val TEXTURE_WRAP_R: Int

    fun clear(bitmask: Int)
    fun clearColor(r: Float, g: Float, b: Float, a: Float)

    fun viewport(x: Int, y: Int, w: Int, h: Int)

    fun bufferInit(): BufferID
    fun bufferRelease(id: BufferID)
    fun bufferBind(type: Int, id: BufferID)
    fun bufferBindLocation(type: Int, id: BufferID, location: Int)
    fun bufferData(type: Int, offset: Int, data: BigBuffer, size: Int, usage: Int)
    fun bufferSubData(type: Int, offset: Int, data: BigBuffer, size: Int)

    fun vertexArrayInit(): VertexArrayID
    fun vertexArrayRelease(id: VertexArrayID)
    fun vertexArrayBind(id: VertexArrayID)
    fun vertexArrayEnableAttributes(attributes: List<VertexAttribute>)
    fun vertexArrayDisableAttributes(attributes: List<VertexAttribute>)

    fun shaderStageInit(type: Int): ShaderStageID
    fun shaderStageRelease(id: ShaderStageID)
    fun shaderStageCompile(id: ShaderStageID, source: String): Boolean
    fun shaderStageAttach(id: ShaderID, shaderStage: ShaderStageID)
    fun shaderStageDetach(id: ShaderID, shaderStage: ShaderStageID)

    fun shaderInit(): ShaderID
    fun shaderRelease(id: ShaderID)
    fun shaderLink(id: ShaderID): Boolean
    fun shaderUse(id: ShaderID)

    fun textureInit(type: Int): TextureID
    fun textureRelease(id: TextureID)
    fun textureBind(type: Int, id: TextureID)
    fun textureUnbind(type: Int)
    fun textureActive(slot: Int)
    fun textureParameter(type: Int, name: Int, value: Int)
    fun textureGenerateMipmap(type: Int)
    fun textureImage2D(type: Int, texture: Texture)

    fun frameBufferInit(): FrameBufferID
    fun frameBufferRelease(id: FrameBufferID)
    fun frameBufferBind(type: Int, id: FrameBufferID)
    fun frameBufferUnbind(type: Int)
    fun frameBufferBlit(
        srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int,
        destX: Int, destY: Int, destWidth: Int, destHeight: Int,
        bitmask: Int, filter: Int
    )
    fun frameBufferCheckStatus(): Boolean
    fun frameBufferAttachColor(
        index: Int,
        textureType: Int,
        id: TextureID,
        textureLevel: Int
    )
    fun frameBufferAttachDepth(
        textureType: Int,
        id: TextureID,
        textureLevel: Int
    )

    fun drawArrays(mode: Int, first: Int, count: Int)
    fun drawArraysInstanced(mode: Int, first: Int, count: Int, instances: Int)
    fun drawElements(mode: Int, indices: Int, type: Int, indicesOffset: Int)
    fun drawElementsInstanced(mode: Int, indices: Int, type: Int, indicesOffset: Int, instances: Int)

}