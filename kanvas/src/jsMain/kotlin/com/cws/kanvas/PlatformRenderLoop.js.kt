package com.cws.kanvas

import kotlinx.browser.window

actual open class PlatformRenderLoop actual constructor(name: String, priority: Int) {

    protected actual var running: Boolean = false

    private var callCreate = false
    private var callDestroy = false
    private var prevTimeMillis = 0.0

    actual fun startLoop() {
        if (running) return
        running = true
        prevTimeMillis = window.performance.now()
        callCreate = true
        nextFrame()
    }

    actual fun stopLoop() {
        if (!running) return
        running = false
        callDestroy = true
    }

    private fun nextFrame() {
        if (!running) {
            if (callDestroy) {
                callDestroy = false
                onDestroy()
            }
            return
        }

        window.requestAnimationFrame { timeMillis ->
            if (callCreate) {
                callCreate = false
                onCreate()
            }
            val dtMillis = timeMillis - prevTimeMillis
            prevTimeMillis = timeMillis
            onUpdate(dtMillis.toFloat())
            nextFrame()
        }
    }

    protected actual open fun onCreate() = Unit
    protected actual open fun onDestroy() = Unit
    protected actual open fun onUpdate(dtMillis: Float) = Unit

}