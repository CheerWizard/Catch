package com.cws.kanvas.core

import com.cws.kanvas.texture.Texture
import com.cws.kanvas.pipeline.VertexAttribute
import com.cws.klog.KLog
import com.cws.fmm.BigBuffer
import kotlinx.browser.document
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

    actual const val NULL: Int = -1
    actual const val STATIC_DRAW: Int = WebGLRenderingContext.STATIC_DRAW
    actual const val DYNAMIC_DRAW: Int = WebGLRenderingContext.DYNAMIC_DRAW
    actual const val FLOAT: Int = WebGLRenderingContext.FLOAT
    actual const val INT: Int = WebGLRenderingContext.INT
    actual const val UINT: Int = WebGLRenderingContext.UNSIGNED_INT
    actual const val BOOLEAN: Int = WebGLRenderingContext.BOOL
    actual const val UBYTE: Int = WebGLRenderingContext.UNSIGNED_BYTE
    actual const val TRIANGLES: Int = WebGLRenderingContext.TRIANGLES
    actual const val VERTEX_BUFFER: Int = WebGLRenderingContext.ARRAY_BUFFER
    actual const val INDEX_BUFFER: Int = WebGLRenderingContext.ELEMENT_ARRAY_BUFFER
    actual const val UNIFORM_BUFFER: Int = NULL
    actual const val FRAME_BUFFER: Int = WebGLRenderingContext.FRAMEBUFFER
    actual const val READ_FRAME_BUFFER: Int = NULL
    actual const val DRAW_FRAME_BUFFER: Int = NULL
    actual const val VERTEX_SHADER: Int = WebGLRenderingContext.VERTEX_SHADER
    actual const val FRAGMENT_SHADER: Int = WebGLRenderingContext.FRAGMENT_SHADER
    actual const val GEOMETRY_SHADER: Int = NULL
    actual const val TESS_CONTROL_SHADER: Int = NULL
    actual const val TESS_EVAL_SHADER: Int = NULL
    actual const val COMPUTE_SHADER: Int = NULL
    actual const val COMPILE_STATUS: Int = WebGLRenderingContext.COMPILE_STATUS
    actual const val LINK_STATUS: Int = WebGLRenderingContext.LINK_STATUS
    actual const val COLOR_BUFFER_BIT: Int = WebGLRenderingContext.COLOR_BUFFER_BIT
    actual const val DEPTH_BUFFER_BIT: Int = WebGLRenderingContext.DEPTH_BUFFER_BIT
    actual const val STENCIL_BUFFER_BIT: Int = WebGLRenderingContext.STENCIL_BUFFER_BIT
    actual const val FORMAT_RGBA: Int = WebGLRenderingContext.RGBA
    actual const val FORMAT_RGB: Int = WebGLRenderingContext.RGB
    actual const val LINEAR: Int = WebGLRenderingContext.LINEAR
    actual const val CLAMP_TO_EDGE: Int = WebGLRenderingContext.CLAMP_TO_EDGE
    actual const val REPEAT: Int = WebGLRenderingContext.REPEAT
    actual const val TEXTURE_2D: Int = WebGLRenderingContext.TEXTURE_2D
    actual const val TEXTURE_CUBE_MAP: Int = WebGLRenderingContext.TEXTURE_CUBE_MAP
    actual const val TEXTURE_MIN_FILTER: Int = WebGLRenderingContext.TEXTURE_MIN_FILTER
    actual const val TEXTURE_MAG_FILTER: Int = WebGLRenderingContext.TEXTURE_MAG_FILTER
    actual const val TEXTURE_WRAP_S: Int = WebGLRenderingContext.TEXTURE_WRAP_S
    actual const val TEXTURE_WRAP_T: Int = WebGLRenderingContext.TEXTURE_WRAP_T
    actual const val TEXTURE_WRAP_R: Int = NULL

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

    actual fun bufferRelease(id: BufferID) {
        context.deleteBuffer(id as WebGLBuffer)
    }

    actual fun bufferBind(type: Int, id: BufferID) {
        context.bindBuffer(type, id as WebGLBuffer)
    }

    actual fun bufferBindLocation(type: Int, id: BufferID, location: Int) {
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

    actual fun vertexArrayRelease(id: VertexArrayID) {
        context.deleteVertexArray(id)
    }

    actual fun vertexArrayBind(id: VertexArrayID) {
        context.bindVertexArray(id)
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

    actual fun shaderStageRelease(id: ShaderStageID) {
        context.deleteShader(id as WebGLShader)
    }

    actual fun shaderStageCompile(id: ShaderStageID, source: String): Boolean {
        if (id == NULL) {
            KLog.error("Shader is not created!")
            return false
        }

        context.shaderSource(id as WebGLShader, source)
        context.compileShader(id)
        val compileStatus = context.getShaderParameter(id, COMPILE_STATUS)

        if (compileStatus == null) {
            val log = context.getShaderInfoLog(id)
            shaderStageRelease(id)
            KLog.error("Failed to compile shader: $log")
            return false
        }

        return true
    }

    actual fun shaderStageAttach(id: ShaderID, shaderStage: ShaderStageID) {
        context.attachShader(id as WebGLProgram, shaderStage as WebGLShader)
    }

    actual fun shaderStageDetach(id: ShaderID, shaderStage: ShaderStageID) {
        context.detachShader(id as WebGLProgram, shaderStage as WebGLShader)
    }

    actual fun shaderInit(): ShaderID {
        return context.createProgram() ?: error("Failed to create shader")
    }

    actual fun shaderRelease(id: ShaderID) {
        context.deleteProgram(id as WebGLProgram)
    }

    actual fun shaderLink(id: ShaderID): Boolean {
        context.linkProgram(id as WebGLProgram)
        val linkStatus = context.getProgramParameter(id, LINK_STATUS)
        if (linkStatus == null) {
            KLog.error("Failed to link shader: ${context.getProgramInfoLog(id)}")
            shaderRelease(id)
            return false
        }
        return true
    }

    actual fun shaderUse(id: ShaderID) {
        context.useProgram(id as WebGLProgram)
    }

    actual fun textureInit(type: Int): TextureID {
        return context.createTexture() ?: error("Failed to create texture $type")
    }

    actual fun textureParameter(type: Int, name: Int, value: Int) {
        context.texParameteri(type, name, value)
    }

    actual fun textureRelease(id: TextureID) {
        context.deleteTexture(id as WebGLTexture)
    }

    actual fun textureBind(type: Int, id: TextureID) {
        context.bindTexture(type, id as WebGLTexture)
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
            texture.pixels
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

    actual fun frameBufferRelease(id: FrameBufferID) {
        context.deleteFramebuffer(id as WebGLFramebuffer)
    }

    actual fun frameBufferBind(type: Int, id: FrameBufferID) {
        context.bindFramebuffer(type, id as WebGLFramebuffer)
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
        id: TextureID,
        textureLevel: Int
    ) {
        context.framebufferTexture2D(
            FRAME_BUFFER,
            WebGLRenderingContext.COLOR_ATTACHMENT0 + index,
            textureType,
            id as WebGLTexture,
            textureLevel
        )
    }

    actual fun frameBufferAttachDepth(
        textureType: Int,
        id: TextureID,
        textureLevel: Int
    ) {
        context.framebufferTexture2D(
            FRAME_BUFFER,
            WebGLRenderingContext.DEPTH_ATTACHMENT,
            textureType,
            id as WebGLTexture,
            textureLevel
        )
    }

}