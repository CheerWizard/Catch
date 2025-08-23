package com.cws.kanvas

import com.cws.kmemory.math.Color

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
    fun clearColor(color: Color)

    fun viewport(x: Int, y: Int, w: Int, h: Int)

    fun bufferInit(): BufferID
    fun bufferRelease(buffer: BufferID)
    fun bufferBind(type: Int, buffer: BufferID)
    fun bufferBindLocation(type: Int, buffer: BufferID, location: Int)
    fun bufferData(type: Int, offset: Int, data: Any?, size: Int, usage: Int)
    fun bufferSubData(type: Int, offset: Int, data: Any?, size: Int)

    fun vertexArrayInit(): VertexArrayID
    fun vertexArrayRelease(vertexArray: VertexArrayID)
    fun vertexArrayBind(vertexArray: VertexArrayID)
    fun vertexArrayEnableAttributes(attributes: List<VertexAttribute>)
    fun vertexArrayDisableAttributes(attributes: List<VertexAttribute>)

    fun shaderStageInit(type: Int): ShaderStageID
    fun shaderStageRelease(shaderStage: ShaderStageID)
    fun shaderStageCompile(shaderStage: ShaderStageID, source: String): Boolean
    fun shaderStageAttach(shader: ShaderID, shaderStage: ShaderStageID)
    fun shaderStageDetach(shader: ShaderID, shaderStage: ShaderStageID)

    fun shaderInit(): ShaderID
    fun shaderRelease(shader: ShaderID)
    fun shaderLink(shader: ShaderID): Boolean
    fun shaderUse(shader: ShaderID)

    fun textureInit(type: Int): TextureID
    fun textureRelease(texture: TextureID)
    fun textureBind(type: Int, texture: TextureID)
    fun textureUnbind(type: Int)
    fun textureActive(slot: Int)
    fun textureParameter(type: Int, name: Int, value: Int)
    fun textureGenerateMipmap(type: Int)
    fun textureImage2D(type: Int, texture: Texture)

    fun frameBufferInit(): FrameBufferID
    fun frameBufferRelease(frameBufferID: FrameBufferID)
    fun frameBufferBind(type: Int, frameBufferID: FrameBufferID)
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
        textureID: TextureID,
        textureLevel: Int
    )
    fun frameBufferAttachDepth(
        textureType: Int,
        textureID: TextureID,
        textureLevel: Int
    )

    fun drawArrays(mode: Int, first: Int, count: Int)
    fun drawArraysInstanced(mode: Int, first: Int, count: Int, instances: Int)
    fun drawElements(mode: Int, indices: Int, type: Int, indicesOffset: Int)
    fun drawElementsInstanced(mode: Int, indices: Int, type: Int, indicesOffset: Int, instances: Int)

}