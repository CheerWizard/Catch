package com.cws.kanvas

import kotlinx.cinterop.ExperimentalForeignApi
import platform.QuartzCore.CAEAGLLayer
import platform.UIKit.UIEvent
import platform.UIKit.UITouch
import platform.UIKit.UIView

class KanvasView(
    private val renderLoop: RenderLoop
) : UIView() {

    init {
        val layer = layer as CAEAGLLayer
        layer.opaque = false
        userInteractionEnabled = true
        multipleTouchEnabled = true
        renderLoop.setSurface(layer)
        renderLoop.startLoop()
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun touchesBegan(touches: Set<*>, withEvent: UIEvent?) {
        val touch = touches.firstOrNull() ?: return
        touch as UITouch
        val point = touch.locationInView(this)
        renderLoop.onMotionEvent(point)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun touchesMoved(touches: Set<*>, withEvent: UIEvent?) {
        val touch = touches.firstOrNull() ?: return
        touch as UITouch
        val point = touch.locationInView(this)
        renderLoop.onMotionEvent(point)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun touchesEnded(touches: Set<*>, withEvent: UIEvent?) {
        val touch = touches.firstOrNull() ?: return
        touch as UITouch
        val point = touch.locationInView(this)
        renderLoop.onMotionEvent(point)
    }

}