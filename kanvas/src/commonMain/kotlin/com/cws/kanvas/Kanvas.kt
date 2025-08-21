package com.cws.kanvas

import com.cws.kmemory.math.Color

expect class VertexArrayID
expect class BufferID
expect class TextureID
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

    val VERTEX_BUFFER: Int
    val INDEX_BUFFER: Int
    val UNIFORM_BUFFER: Int
    val FRAME_BUFFER: Int

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

}