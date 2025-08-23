package com.cws.kanvas

import org.khronos.webgl.WebGLRenderingContext

abstract external class WebGL2RenderingContext : WebGLRenderingContext {
    fun createVertexArray(): dynamic
    fun bindVertexArray(array: dynamic)
    fun deleteVertexArray(array: dynamic)
    fun drawArraysInstanced(mode: Int, first: Int, count: Int, instanceCount: Int)
    fun drawElementsInstanced(mode: Int, indices: Int, type: Int, indicesOffset: Int, instanceCount: Int)
    fun vertexAttribDivisor(index: Int, divisor: Int)
}