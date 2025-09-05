package com.cws.kanvas

import android.content.Context
import android.graphics.SurfaceTexture
import android.view.MotionEvent
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.cws.klog.KLog

// replaced by Composable entry point for Android platform
actual class KanvasEntryPoint {
    actual constructor(renderLoop: RenderLoop, content: @Composable ((RenderLoop) -> Unit))
}

@Composable
fun KanvasEntryPoint(
    modifier: Modifier,
    renderLoop: RenderLoop,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
    ) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                KanvasView(
                    context = context,
                    renderLoop = renderLoop
                )
            }
        )
        content()
    }
}

class KanvasView(
    context: Context,
    private var renderLoop: RenderLoop? = null
) : TextureView(context), TextureView.SurfaceTextureListener {

    init {
        focusable = FOCUSABLE_AUTO
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        KLog.debug()
        renderLoop?.onViewportChanged(width = width, height = height, format = 0)
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        KLog.debug()
        renderLoop?.let {
            it.setSurface(Surface(surface))
            it.startLoop()
        }
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        KLog.debug()
        renderLoop?.stopLoop()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) = Unit

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        KLog.debug()
        renderLoop?.onMotionEvent(event)
        return super.onTouchEvent(event)
    }

}