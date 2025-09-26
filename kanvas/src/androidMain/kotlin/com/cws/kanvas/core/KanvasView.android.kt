package com.cws.kanvas.core

import android.content.Context
import android.graphics.SurfaceTexture
import android.view.MotionEvent
import android.view.Surface
import android.view.TextureView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.cws.printer.Printer
import org.koin.compose.koinInject

@Composable
inline fun <reified T : RenderLoop> KanvasView(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val renderLoop: T = koinInject()
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
        surfaceTextureListener = this
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        Printer.d()
        renderLoop?.onViewportChanged(width = width, height = height)
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        Printer.d()
        renderLoop?.let {
            it.setSurface(Surface(surface))
            it.startLoop()
        }
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        Printer.d()
        renderLoop?.stopLoop()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        Printer.d()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Printer.d()
        renderLoop?.onMotionEvent(event)
        return super.onTouchEvent(event)
    }

}