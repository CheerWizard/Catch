package com.cws.kanvas

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

    protected val renderers: MutableSet<Renderer> = mutableSetOf()
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
        renderers.forEach { it.init() }
    }

    override fun onDestroy() {
        renderers.forEach { it.release() }
        vertexArray.release()
        vertexBuffer.release()
        indexBuffer.release()
        window.release()
    }

    override fun onUpdate(dtMillis: Float) {
        KLog.debug("dt=${dtMillis}ms FPS=${dtMillis.fps}")
        window.pollEvents()
        onFrameUpdate(dtMillis)
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
        renderers.forEach { it.render() }
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