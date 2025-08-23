package com.cws.kanvas

import android.content.Context
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class KanvasView(
    context: Context,
    private var renderLoop: RenderLoop? = null
) : SurfaceView(context), SurfaceHolder.Callback {

    init {
        holder.addCallback(this)
        focusable = FOCUSABLE_AUTO
    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int,
    ) {
        renderLoop?.onSurfaceChanged(width = width, height = height, format = format)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (renderLoop != null) {
            renderLoop = RenderLoop(surface = holder.surface)
            renderLoop?.startLoop()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        renderLoop?.stopLoop()
        renderLoop = null
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        renderLoop?.onMotionEvent(event)
        return super.onTouchEvent(event)
    }

}