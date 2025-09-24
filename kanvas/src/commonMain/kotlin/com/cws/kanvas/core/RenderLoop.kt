package com.cws.kanvas.core

import com.cws.kanvas.pipeline.IndexBuffer
import com.cws.kanvas.pipeline.VERTEX_ATTRIBUTES
import com.cws.kanvas.pipeline.VertexArray
import com.cws.kanvas.pipeline.VertexBuffer
import com.cws.kanvas.pipeline.Viewport
import com.cws.kanvas.utils.fps
import com.cws.klog.KLog

abstract class RenderLoop(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    protected val title: String,
) : PlatformRenderLoop(name = "RenderLoop", priority = 1) {

    lateinit var window: Window
        protected set

    protected val vertexArray: VertexArray = VertexArray(VERTEX_ATTRIBUTES)
    protected val vertexBuffer: VertexBuffer = VertexBuffer(1000)
    protected val indexBuffer: IndexBuffer = IndexBuffer(1000)

    private val viewport = Viewport(x, y, width, height)

    private var surface: Any? = null

    lateinit var onWindowCreated: (Window) -> Unit

    override fun onCreate() {
        window = Window(x, y, width, height, title)
        window.setSurface(surface)
        if (::onWindowCreated.isInitialized) {
            onWindowCreated(window)
        }
        vertexArray.init()
        vertexBuffer.init()
        indexBuffer.init()
    }

    override fun onDestroy() {
        vertexArray.release()
        vertexBuffer.release()
        indexBuffer.release()
        window.release()
    }

    override fun onUpdate(dtMillis: Float) {
        KLog.debug("dt=${dtMillis}ms FPS=${dtMillis.fps}")
        window.pollEvents()
        onFrameUpdate(dtMillis)
        RenderLoopJobs.execute()
        render(dtMillis)
        window.applySwapChain()
        running = !window.isClosed()
    }

    private fun render(dt: Float) {
        Kanvas.run {
            clearColor(0.5f, 0f, 0f, 1f)
            clear(COLOR_BUFFER_BIT or DEPTH_BUFFER_BIT)
        }
        vertexArray.bind()
        onRender(dt)
    }

    protected abstract fun onFrameUpdate(dt: Float)
    protected abstract fun onRender(dt: Float)

    fun onViewportChanged(width: Int, height: Int) {
        viewport.width = width
        viewport.height = height
    }

    fun onMotionEvent(event: Any?) {
        if (event != null && ::window.isInitialized) {
            window.addEvent(event)
        }
    }

    fun setSurface(surface: Any?) {
        this.surface = surface
    }

}