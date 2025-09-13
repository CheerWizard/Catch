package com.cws.kanvas

import android.opengl.EGL14.*
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.opengl.EGLDisplay
import android.opengl.EGLSurface
import android.view.MotionEvent
import kotlinx.atomicfu.locks.ReentrantLock

actual typealias WindowID = Unit

actual class Window : BaseWindow {

    actual companion object {
        private const val EGL_OPENGL_ES3_BIT: Int = 0x00000040

        actual fun free() = Unit
    }

    actual override val eventListeners: MutableSet<EventListener> = mutableSetOf()
    actual override val events: ArrayDeque<Any> = ArrayDeque()
    actual override val lock: ReentrantLock = ReentrantLock()

    private var display: EGLDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY)
    private var surface: EGLSurface? = null
    private var context: EGLContext? = null

    actual constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        title: String
    )

    actual fun release() {
        eglDestroySurface(display, surface)
        eglDestroyContext(display, context)
        eglTerminate(display)
    }

    actual fun isClosed(): Boolean = false

    actual fun applySwapChain() {
        eglSwapBuffers(display, surface)
    }

    actual fun setSurface(surface: Any?) {
        if (surface == null) return

        val version = IntArray(2)
        eglInitialize(display, version, 0, version, 1)

        val extensions = eglQueryString(display, EGL_EXTENSIONS)

        val eglVersionBit: Int = if (extensions.contains("EGL_KHR_create_context")) {
            EGL_OPENGL_ES3_BIT
        } else {
            EGL_OPENGL_ES2_BIT
        }

        val attribList = intArrayOf(
            EGL_RENDERABLE_TYPE,
            eglVersionBit,
            EGL_RED_SIZE, 8,
            EGL_GREEN_SIZE, 8,
            EGL_BLUE_SIZE, 8,
            EGL_ALPHA_SIZE, 8,
            EGL_DEPTH_SIZE, 16,
            EGL_NONE
        )

        val configs = arrayOfNulls<EGLConfig>(1)
        val numConfigs = IntArray(1)
        eglChooseConfig(display, attribList, 0, configs, 0, 1, numConfigs, 0)
        val eglConfig = configs[0]

        val contextAttributes = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, 3, EGL_NONE)
        this.context = eglCreateContext(display, eglConfig, EGL_NO_CONTEXT, contextAttributes, 0)

        val surfaceAttributes = intArrayOf(EGL_NONE)
        this.surface = eglCreateWindowSurface(display, eglConfig, surface, surfaceAttributes, 0)

        eglMakeCurrent(display, this.surface, this.surface, context)
    }

    override fun dispatchEvent(event: Any) {
        if (event is MotionEvent) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> eventListeners.forEach { it.onTapPressed(event.x, event.y) }
                MotionEvent.ACTION_UP -> eventListeners.forEach { it.onTapReleased(event.x, event.y) }
            }
        }
    }

}