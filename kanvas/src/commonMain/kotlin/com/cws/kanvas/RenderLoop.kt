package com.cws.kanvas

abstract class RenderLoop(
    private val width: Int = 800,
    private val height: Int = 600,
    private val title: String = "",
    private val surface: Any? = null
) : PlatformRenderLoop(name = "RenderLoop", priority = 1) {

    protected lateinit var window: Window
    protected val renderers: MutableSet<Renderer> = mutableSetOf()
    protected val vertexArray: VertexArray = VertexArray(Vertices.ATTRIBUTES)
    protected val vertexBuffer: VertexBuffer = VertexBuffer(1000)
    protected val indexBuffer: IndexBuffer = IndexBuffer(1000)

    private val viewport = Viewport(0, 0, width, height)

    override fun onCreate() {
        window = Window(width, height, title, surface)
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
        window.pollEvents()
        onFrameUpdate(dtMillis)
        render(dtMillis)
        window.applySwapChain()
        running = !window.isClosed()
    }

    private fun render(dt: Float) {
        Kanvas.run {
            viewport(viewport.x, viewport.y, viewport.width, viewport.height)
            clearColor(0f, 0f, 0f, 1f)
            clear(COLOR_BUFFER_BIT or DEPTH_BUFFER_BIT)
        }
        vertexArray.bind()
        renderers.forEach { it.render() }
        onRender(dt)
    }

    protected abstract fun onFrameUpdate(dt: Float)
    protected abstract fun onRender(dt: Float)

    fun onSurfaceChanged(width: Int, height: Int, format: Int) {
        viewport.width = width
        viewport.height = height
    }

    fun onMotionEvent(event: Any?) {
        if (event != null) {
            window.addEvent(event)
        }
    }

}