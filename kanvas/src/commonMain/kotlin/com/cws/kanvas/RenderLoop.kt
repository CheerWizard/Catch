package com.cws.kanvas

import com.cws.kmemory.math.Color

open class RenderLoop(
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
        window.pollEvents {}
        render(dtMillis)
        window.applySwapChain()
        running = !window.isClosed()
    }

    private fun render(dtMillis: Float) {
        Kanvas.run {
            viewport(viewport.x, viewport.y, viewport.width, viewport.height)
            clearColor(Color.Black)
            clear(COLOR_BUFFER_BIT or DEPTH_BUFFER_BIT)
        }

        vertexArray.bind()

        renderers.forEach { it.render() }

        //        // debug screen
//        drawRect(
//            topLeft = Offset(screenBox.x, screenBox.y),
//            size = Size(screenBox.w, screenBox.h),
//            color = Color.Red,
//            style = Stroke(width = 4f)
//        )
//
//        // debug grid
//        var x = 0f
//        var y = 0f
//        repeat(grid.rows) { i ->
//            repeat(grid.cols) { j ->
//                drawRect(
//                    topLeft = Offset(x, y),
//                    size = Size(grid.cellSize.toFloat(), grid.cellSize.toFloat()),
//                    color = Color.Green,
//                    style = Stroke(width = 4f)
//                )
//                x += grid.cellSize.toFloat()
//            }
//            y += grid.cellSize.toFloat()
//        }
//
//        repeat(projectiles.x.size) { i ->
//            if (projectiles.visible[i]) {
//                drawLine(
//                    color = Color(projectiles.color[i]),
//                    start = Offset(projectiles.x[i], projectiles.y[i]),
//                    end = Offset(projectiles.x[i], projectiles.y[i] + projectiles.l[i]),
//                    strokeWidth = 16f
//                )
//            }
//        }
    }

    fun onSurfaceChanged(width: Int, height: Int, format: Int) {
        viewport.width = width
        viewport.height = height
    }

    fun onMotionEvent(event: Any?) {
        window.onMotionEvent(event)
    }

}