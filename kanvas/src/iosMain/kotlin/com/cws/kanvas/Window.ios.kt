package com.cws.kanvas

import kotlinx.cinterop.memScoped
import platform.EAGL.EAGLContext
import platform.EAGL.kEAGLRenderingAPIOpenGLES3
import platform.EAGL.presentRenderbuffer
import platform.QuartzCore.CAEAGLLayer
import platform.gles3.GL_FRAMEBUFFER
import platform.gles3.GL_RENDERBUFFER
import platform.gles3.glBindRenderbuffer
import platform.gles3.glDeleteFramebuffers
import platform.gles3.glDeleteRenderbuffers
import platform.gles3.glGenRenderbuffers

actual typealias WindowID = Unit

actual class Window : BaseWindow {

    actual companion object {
        actual fun free() = Unit
    }

    private val context = EAGLContext(kEAGLRenderingAPIOpenGLES3)

    private var framebuffer: UInt = 0u
    private var renderbuffer: UInt = 0u

    private var layer: CAEAGLLayer? = null

    actual constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        title: String
    ) {
        if (layer == null) return
        EAGLContext.setCurrentContext(context)
        memScoped {
            val rb = alloc<UIntVar>()
            glGenRenderbuffers(1, rb.ptr)
            renderbuffer = rb.value
            glBindRenderbuffer(GL_RENDERBUFFER.toUInt(), renderbuffer)
            context.renderbufferStorage(GL_RENDERBUFFER.toUInt(), layer)

            val fb = alloc<UIntVar>()
            glGenFramebuffers(1, fb.ptr)
            framebuffer = fb.value
            glBindFramebuffer(GL_FRAMEBUFFER.toUInt(), framebuffer)
            glFramebufferRenderbuffer(
                GL_FRAMEBUFFER.toUInt(),
                GL_COLOR_ATTACHMENT0.toUInt(),
                GL_RENDERBUFFER.toUInt(),
                renderbuffer
            )
        }
    }

    actual fun release() {
        glDeleteRenderbuffers(1, renderbuffer)
        glDeleteFramebuffers(1, framebuffer)
        context.finalize()
    }

    actual fun isClosed(): Boolean = false

    actual fun bindFrameBuffer() {
        EAGLContext.setCurrentContext(context)
        glBindFrameBuffer(GL_FRAMEBUFFER.toUInt(), framebuffer)
    }

    actual fun applySwapChain() {
        context.presentRenderbuffer(GL_RENDERBUFFER.toUInt())
    }

    actual fun setSurface(surface: Any?) {
        layer = surface as CAEAGLLayer?
    }

    override fun dispatchEvent(event: Any) {
        super.dispatchEvent(event)
    }

}