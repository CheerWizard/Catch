package com.cws.kanvas

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.free
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.EAGL.EAGLContext
import platform.EAGL.kEAGLRenderingAPIOpenGLES3
import platform.EAGL.presentRenderbuffer
import platform.EAGL.renderbufferStorage
import platform.QuartzCore.CAEAGLLayer
import platform.gles3.GL_COLOR_ATTACHMENT0
import platform.gles3.GL_FRAMEBUFFER
import platform.gles3.GL_RENDERBUFFER
import platform.gles3.glBindFramebuffer
import platform.gles3.glBindRenderbuffer
import platform.gles3.glDeleteFramebuffers
import platform.gles3.glDeleteRenderbuffers
import platform.gles3.glFramebufferRenderbuffer
import platform.gles3.glGenFramebuffers
import platform.gles3.glGenRenderbuffers

actual typealias WindowID = Unit

actual class Window : BaseWindow {

    actual companion object {
        actual fun free() = Unit
    }

    private val context = EAGLContext(kEAGLRenderingAPIOpenGLES3)

    @OptIn(ExperimentalForeignApi::class)
    private var framebuffer: CPointer<UIntVar> = nativeHeap.alloc<UIntVar>().ptr
    @OptIn(ExperimentalForeignApi::class)
    private var renderbuffer: CPointer<UIntVar> = nativeHeap.alloc<UIntVar>().ptr

    private var layer: CAEAGLLayer? = null

    @OptIn(ExperimentalForeignApi::class)
    actual constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        title: String
    ) {
        if (layer == null) return
        EAGLContext.setCurrentContext(context)

        glGenRenderbuffers(1, renderbuffer)
        glBindRenderbuffer(GL_RENDERBUFFER.toUInt(), UIntVar(renderbuffer.rawValue).value)
        context.renderbufferStorage(GL_RENDERBUFFER.toULong(), layer)

        glGenFramebuffers(1, framebuffer)
        glBindFramebuffer(GL_FRAMEBUFFER.toUInt(), UIntVar(framebuffer.rawValue).value)
        glFramebufferRenderbuffer(
            GL_FRAMEBUFFER.toUInt(),
            GL_COLOR_ATTACHMENT0.toUInt(),
            GL_RENDERBUFFER.toUInt(),
            UIntVar(renderbuffer.rawValue).value
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun release() {
        glDeleteRenderbuffers(1, renderbuffer)
        glDeleteFramebuffers(1, framebuffer)
        nativeHeap.free(renderbuffer)
        nativeHeap.free(framebuffer)
        context.finalize()
    }

    actual fun isClosed(): Boolean = false

    @OptIn(ExperimentalForeignApi::class)
    actual fun bindFrameBuffer() {
        EAGLContext.setCurrentContext(context)
        glBindFramebuffer(GL_FRAMEBUFFER.toUInt(), UIntVar(framebuffer.rawValue).value)
    }

    actual fun applySwapChain() {
        context.presentRenderbuffer(GL_RENDERBUFFER.toULong())
    }

    actual fun setSurface(surface: Any?) {
        layer = surface as CAEAGLLayer?
    }

    override fun dispatchEvent(event: Any) {
        super.dispatchEvent(event)
    }

}