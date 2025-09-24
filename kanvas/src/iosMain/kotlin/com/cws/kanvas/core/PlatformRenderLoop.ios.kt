package com.cws.kanvas.core

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.CoreFoundation.CFTimeInterval
import platform.Foundation.NSDefaultRunLoopMode
import platform.Foundation.NSRunLoop
import platform.Foundation.NSSelectorFromString
import platform.QuartzCore.CACurrentMediaTime
import platform.QuartzCore.CADisplayLink

actual open class PlatformRenderLoop actual constructor(name: String, priority: Int) {

    protected actual var running: Boolean = false
    private var callCreate = false
    private var callDestroy = false

    private lateinit var displayLink: CADisplayLink
    private var prevTime: CFTimeInterval = 0.0

    @OptIn(ExperimentalForeignApi::class)
    actual fun startLoop() {
        if (running) return
        running = true
        callCreate = true
        prevTime = CACurrentMediaTime()
        displayLink = CADisplayLink.displayLinkWithTarget(this, NSSelectorFromString("doFrame:"))
        displayLink.addToRunLoop(NSRunLoop.mainRunLoop, NSDefaultRunLoopMode)
    }

    actual fun stopLoop() {
        if (!running) return
        running = false
        callDestroy = true
        displayLink.invalidate()
    }

    @OptIn(BetaInteropApi::class)
    @ObjCAction
    fun doFrame(link: CADisplayLink) {
        if (!running) {
            if (callDestroy) {
                callDestroy = false
                onDestroy()
            }
            return
        }

        if (callCreate) {
            callCreate = false
            onCreate()
        }

        val timeMillis = link.timestamp
        val dtMillis = prevTime - timeMillis
        prevTime = timeMillis
        onUpdate(dtMillis.toFloat())
    }

    protected actual open fun onCreate() = Unit
    protected actual open fun onDestroy() = Unit
    protected actual open fun onUpdate(dtMillis: Float) = Unit

}