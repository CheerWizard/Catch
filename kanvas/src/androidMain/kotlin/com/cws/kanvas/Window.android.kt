package com.cws.kanvas

import android.opengl.EGL14.*
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.opengl.EGLDisplay
import android.opengl.EGLSurface
import android.view.MotionEvent

actual typealias WindowID = Unit

actual class Window : BaseWindow {

    actual companion object {
        private const val EGL_OPENGL_ES3_BIT: Int = 0x00000040

        actual fun free() = Unit
    }

    private var display: EGLDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY)
    private var surface: EGLSurface? = null
    private var context: EGLContext? = null

    actual constructor(
        width: Int,
        height: Int,
        title: String,
        surface: Any?
    ) {
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
    }

    actual fun release() {
        eglDestroySurface(display, surface)
        eglDestroyContext(display, context)
        eglTerminate(display)
    }

    actual fun isClosed(): Boolean = false

    actual fun applySwapChain() {
        eglSwapBuffers(display, surface)
    }

    actual inline fun pollEvents(crossinline block: () -> Unit) = Unit

    actual fun setCurrent() {
        eglMakeCurrent(display, surface, surface, context)
    }

    actual fun onMotionEvent(event: Any?) {
        (event as MotionEvent?)?.let { e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> eventListeners.forEach { it.onTapPressed(e.x, e.y) }
                MotionEvent.ACTION_UP -> eventListeners.forEach { it.onTapReleased(e.x, e.y) }
            }
        }
    }

}