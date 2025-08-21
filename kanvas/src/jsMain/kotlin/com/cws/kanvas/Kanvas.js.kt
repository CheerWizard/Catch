package com.cws.kanvas

import com.cws.kmemory.math.Color
import kotlinx.browser.document
import org.khronos.webgl.BufferDataSource
import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLProgram
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLShader
import org.w3c.dom.HTMLCanvasElement

actual typealias VertexArrayID = Any
actual typealias BufferID = Any
actual typealias TextureID = Any
actual typealias ShaderStageID = Any
actual typealias ShaderID = Any

actual object Kanvas {

    actual val NULL: Int = -1
    actual val STATIC_DRAW: Int = WebGLRenderingContext.STATIC_DRAW
    actual val DYNAMIC_DRAW: Int = WebGLRenderingContext.DYNAMIC_DRAW
    actual val FLOAT: Int = WebGLRenderingContext.FLOAT
    actual val INT: Int = WebGLRenderingContext.INT
    actual val UINT: Int = WebGLRenderingContext.UNSIGNED_INT
    actual val BOOLEAN: Int = WebGLRenderingContext.BOOL
    actual val VERTEX_BUFFER: Int = WebGLRenderingContext.ARRAY_BUFFER
    actual val INDEX_BUFFER: Int = WebGLRenderingContext.ELEMENT_ARRAY_BUFFER
    actual val UNIFORM_BUFFER: Int = NULL
    actual val FRAME_BUFFER: Int = WebGLRenderingContext.FRAMEBUFFER
    actual val VERTEX_SHADER: Int = WebGLRenderingContext.VERTEX_SHADER
    actual val FRAGMENT_SHADER: Int = WebGLRenderingContext.FRAGMENT_SHADER
    actual val GEOMETRY_SHADER: Int = NULL
    actual val TESS_CONTROL_SHADER: Int = NULL
    actual val TESS_EVAL_SHADER: Int = NULL
    actual val COMPUTE_SHADER: Int = NULL
    actual val COMPILE_STATUS: Int = WebGLRenderingContext.COMPILE_STATUS
    actual val LINK_STATUS: Int = WebGLRenderingContext.LINK_STATUS
    actual val COLOR_BUFFER_BIT: Int = WebGLRenderingContext.COLOR_BUFFER_BIT
    actual val DEPTH_BUFFER_BIT: Int = WebGLRenderingContext.DEPTH_BUFFER_BIT
    actual val STENCIL_BUFFER_BIT: Int = WebGLRenderingContext.STENCIL_BUFFER_BIT

    val context = createContext()

    private fun createContext(canvasId: String = "canvas"): WebGL2RenderingContext {
        val canvas = document.getElementById("canvas") as HTMLCanvasElement?
            ?: error("Canvas element with id $canvasId is not found")

        val context = canvas.getContext("webgl2") as WebGL2RenderingContext?
            ?: error("Failed to initialize WebGL2")

        if (context.asDynamic().createVertexArray == undefined) {
            error("Failed to initialize WebGL2")
        }

        return context
    }

    actual fun clear(bitmask: Int) {
        context.clear(bitmask)
    }

    actual fun clearColor(color: Color) {
        val vec4 = color.toVec4()
        context.clearColor(vec4.x, vec4.y, vec4.z, vec4.w)
    }

    actual fun viewport(x: Int, y: Int, w: Int, h: Int) {
        context.viewport(x, y, w, h)
    }

    actual fun bufferInit(): BufferID {
        return context.createBuffer() ?: error("Failed to create WebGL buffer!")
    }

    actual fun bufferRelease(buffer: BufferID) {
        context.deleteBuffer(buffer as WebGLBuffer)
    }

    actual fun bufferBind(type: Int, buffer: BufferID) {
        context.bindBuffer(type, buffer as WebGLBuffer)
    }

    actual fun bufferBindLocation(type: Int, buffer: BufferID, location: Int) {
//        context.bind(type, buffer as WebGLBuffer)
    }

    actual fun bufferData(
        type: Int,
        offset: Int,
        data: Any?,
        size: Int,
        usage: Int
    ) {
        if (data == null) {
            context.bufferData(type, size, usage)
        } else {
            context.bufferData(type, data as BufferDataSource?, usage)
        }
    }

    actual fun bufferSubData(
        type: Int,
        offset: Int,
        data: Any?,
        size: Int,
    ) {
        context.bufferSubData(type, offset, data as BufferDataSource?)
    }

    actual fun vertexArrayInit(): VertexArrayID {
        return context.createVertexArray()
    }

    actual fun vertexArrayRelease(vertexArray: VertexArrayID) {
        context.deleteVertexArray(vertexArray)
    }

    actual fun vertexArrayBind(vertexArray: VertexArrayID) {
        context.bindVertexArray(vertexArray)
    }

    actual fun vertexArrayEnableAttributes(attributes: List<VertexAttribute>) {
        var attributeOffset = 0
        attributes.forEach { attribute ->
            context.enableVertexAttribArray(attribute.location)
            context.vertexAttribPointer(
                attribute.location,
                attribute.type.size,
                attribute.type.type,
                false,
                attribute.type.stride,
                attributeOffset
            )
            context.vertexAttribDivisor(
                attribute.location,
                if (attribute.enableInstancing) 1 else 0
            )
            attributeOffset += attribute.type.stride
        }
    }

    actual fun vertexArrayDisableAttributes(attributes: List<VertexAttribute>) {
        attributes.forEach { attribute ->
            context.disableVertexAttribArray(attribute.location)
        }
    }

    actual fun shaderStageInit(type: Int): ShaderStageID {
        return context.createShader(type) ?: error("Failed to create shader stage $type")
    }

    actual fun shaderStageRelease(shaderStage: ShaderStageID) {
        context.deleteShader(shaderStage as WebGLShader)
    }

    actual fun shaderStageCompile(shaderStage: ShaderStageID, source: String): Boolean {
        if (shaderStage == NULL) {
            IllegalArgumentException("Shader is not created!").printStackTrace()
            return false
        }

        context.shaderSource(shaderStage as WebGLShader, source)
        context.compileShader(shaderStage)
        val compileStatus = context.getShaderParameter(shaderStage, COMPILE_STATUS)

        if (compileStatus == null) {
            val log = context.getShaderInfoLog(shaderStage)
            shaderStageRelease(shaderStage)
            RuntimeException("Failed to compile shader: $log").printStackTrace()
            return false
        }

        return true
    }

    actual fun shaderStageAttach(shader: ShaderID, shaderStage: ShaderStageID) {
        context.attachShader(shader as WebGLProgram, shaderStage as WebGLShader)
    }

    actual fun shaderStageDetach(shader: ShaderID, shaderStage: ShaderStageID) {
        context.detachShader(shader as WebGLProgram, shaderStage as WebGLShader)
    }

    actual fun shaderInit(): ShaderID {
        return context.createProgram() ?: error("Failed to create shader")
    }

    actual fun shaderRelease(shader: ShaderID) {
        context.deleteProgram(shader as WebGLProgram)
    }

    actual fun shaderLink(shader: ShaderID): Boolean {
        context.linkProgram(shader as WebGLProgram)
        val linkStatus = context.getProgramParameter(shader, LINK_STATUS)
        if (linkStatus == null) {
            RuntimeException("Failed to link shader: ${context.getProgramInfoLog(shader)}").printStackTrace()
            shaderRelease(shader)
            return false
        }
        return true
    }

    actual fun shaderUse(shader: ShaderID) {
        context.useProgram(shader as WebGLProgram)
    }

}