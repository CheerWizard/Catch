package com.cws.kanvas

import com.cws.klog.KLog
import com.cws.kmemory.BigBuffer
import kotlinx.browser.document
import org.khronos.webgl.ArrayBufferView
import org.khronos.webgl.BufferDataSource
import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLFramebuffer
import org.khronos.webgl.WebGLProgram
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLShader
import org.khronos.webgl.WebGLTexture
import org.w3c.dom.HTMLCanvasElement

actual typealias VertexArrayID = Any
actual typealias BufferID = Any
actual typealias TextureID = Any
actual typealias FrameBufferID = Any
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
    actual val UBYTE: Int = WebGLRenderingContext.UNSIGNED_BYTE
    actual val TRIANGLES: Int = WebGLRenderingContext.TRIANGLES
    actual val VERTEX_BUFFER: Int = WebGLRenderingContext.ARRAY_BUFFER
    actual val INDEX_BUFFER: Int = WebGLRenderingContext.ELEMENT_ARRAY_BUFFER
    actual val UNIFORM_BUFFER: Int = NULL
    actual val FRAME_BUFFER: Int = WebGLRenderingContext.FRAMEBUFFER
    actual val READ_FRAME_BUFFER: Int = NULL
    actual val DRAW_FRAME_BUFFER: Int = NULL
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
    actual val FORMAT_RGBA: Int = WebGLRenderingContext.RGBA
    actual val FORMAT_RGB: Int = WebGLRenderingContext.RGB
    actual val LINEAR: Int = WebGLRenderingContext.LINEAR
    actual val CLAMP_TO_EDGE: Int = WebGLRenderingContext.CLAMP_TO_EDGE
    actual val REPEAT: Int = WebGLRenderingContext.REPEAT
    actual val TEXTURE_2D: Int = WebGLRenderingContext.TEXTURE_2D
    actual val TEXTURE_CUBE_MAP: Int = WebGLRenderingContext.TEXTURE_CUBE_MAP
    actual val TEXTURE_MIN_FILTER: Int = WebGLRenderingContext.TEXTURE_MIN_FILTER
    actual val TEXTURE_MAG_FILTER: Int = WebGLRenderingContext.TEXTURE_MAG_FILTER
    actual val TEXTURE_WRAP_S: Int = WebGLRenderingContext.TEXTURE_WRAP_S
    actual val TEXTURE_WRAP_T: Int = WebGLRenderingContext.TEXTURE_WRAP_T
    actual val TEXTURE_WRAP_R: Int = NULL

    private val context: WebGL2RenderingContext = createContext()

    private fun createContext(): WebGL2RenderingContext {
        val context = getCanvas()?.getContext("webgl2") as WebGL2RenderingContext?
            ?: error("Failed to initialize WebGL2")

        if (context.asDynamic().createVertexArray == undefined) {
            error("Failed to initialize WebGL2")
        }

        return context
    }

    fun getCanvas(): HTMLCanvasElement? {
        return document.getElementById(KANVAS_RUNTIME) as HTMLCanvasElement?
            ?: error("Canvas element with id $KANVAS_RUNTIME is not found")
    }

    actual fun clear(bitmask: Int) {
        context.clear(bitmask)
    }

    actual fun clearColor(r: Float, g: Float, b: Float, a: Float) {
        context.clearColor(r, g, b, a)
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
        data: BigBuffer,
        size: Int,
        usage: Int
    ) {
        context.bufferData(type, data.buffer, usage)
    }

    actual fun bufferSubData(
        type: Int,
        offset: Int,
        data: BigBuffer,
        size: Int,
    ) {
        context.bufferSubData(type, offset, data.buffer)
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
            KLog.error("Shader is not created!")
            return false
        }

        context.shaderSource(shaderStage as WebGLShader, source)
        context.compileShader(shaderStage)
        val compileStatus = context.getShaderParameter(shaderStage, COMPILE_STATUS)

        if (compileStatus == null) {
            val log = context.getShaderInfoLog(shaderStage)
            shaderStageRelease(shaderStage)
            KLog.error("Failed to compile shader: $log")
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
            KLog.error("Failed to link shader: ${context.getProgramInfoLog(shader)}")
            shaderRelease(shader)
            return false
        }
        return true
    }

    actual fun shaderUse(shader: ShaderID) {
        context.useProgram(shader as WebGLProgram)
    }

    actual fun textureInit(type: Int): TextureID {
        return context.createTexture() ?: error("Failed to create texture $type")
    }

    actual fun textureParameter(type: Int, name: Int, value: Int) {
        context.texParameteri(type, name, value)
    }

    actual fun textureRelease(texture: TextureID) {
        context.deleteTexture(texture as WebGLTexture)
    }

    actual fun textureBind(type: Int, texture: TextureID) {
        context.bindTexture(type, texture as WebGLTexture)
    }

    actual fun textureUnbind(type: Int) {
        context.bindTexture(type, null)
    }

    actual fun textureActive(slot: Int) {
        context.activeTexture(WebGLRenderingContext.TEXTURE0 + slot)
    }

    actual fun textureGenerateMipmap(type: Int) {
        context.generateMipmap(type)
    }

    actual fun textureImage2D(type: Int, texture: Texture) {
        context.texImage2D(
            type,
            texture.mipLevel,
            texture.format,
            texture.width,
            texture.height,
            texture.border,
            texture.format,
            texture.pixelFormat,
            texture.pixels as ArrayBufferView
        )
    }

    actual fun drawArrays(mode: Int, first: Int, count: Int) {
        context.drawArrays(mode, first, count)
    }

    actual fun drawArraysInstanced(mode: Int, first: Int, count: Int, instances: Int) {
        context.drawArraysInstanced(mode, first, count, instances)
    }

    actual fun drawElements(mode: Int, indices: Int, type: Int, indicesOffset: Int) {
        context.drawElements(mode, indices, type, indicesOffset)
    }

    actual fun drawElementsInstanced(mode: Int, indices: Int, type: Int, indicesOffset: Int, instances: Int) {
        context.drawElementsInstanced(mode, indices, type, indicesOffset, instances)
    }

    actual fun frameBufferInit(): FrameBufferID {
        return context.createFramebuffer() ?: error("Failed to create frame buffer")
    }

    actual fun frameBufferRelease(frameBufferID: FrameBufferID) {
        context.deleteFramebuffer(frameBufferID as WebGLFramebuffer)
    }

    actual fun frameBufferBind(type: Int, frameBufferID: FrameBufferID) {
        context.bindFramebuffer(type, frameBufferID as WebGLFramebuffer)
    }

    actual fun frameBufferUnbind(type: Int) {
        context.bindFramebuffer(type, null)
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
        // TODO not available in this context
    }

    actual fun frameBufferCheckStatus(): Boolean {
        return context.checkFramebufferStatus(FRAME_BUFFER) == WebGLRenderingContext.FRAMEBUFFER_COMPLETE
    }

    actual fun frameBufferAttachColor(
        index: Int,
        textureType: Int,
        textureID: TextureID,
        textureLevel: Int
    ) {
        context.framebufferTexture2D(
            FRAME_BUFFER,
            WebGLRenderingContext.COLOR_ATTACHMENT0 + index,
            textureType,
            textureID as WebGLTexture,
            textureLevel
        )
    }

    actual fun frameBufferAttachDepth(
        textureType: Int,
        textureID: TextureID,
        textureLevel: Int
    ) {
        context.framebufferTexture2D(
            FRAME_BUFFER,
            WebGLRenderingContext.DEPTH_ATTACHMENT,
            textureType,
            textureID as WebGLTexture,
            textureLevel
        )
    }

}