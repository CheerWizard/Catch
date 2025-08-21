package com.cws.kanvas

class KanvasRender(
    private val shader: Shader,
    private val vertexArray: VertexArray,
    private val vertexBuffer: VertexBuffer,
    private val indexBuffer: IndexBuffer
) : GLSurfaceView.Renderer {

    private var scene: GameScene? = null

    fun setScene(scene: GameScene) {
        this.scene = scene
    }

    fun init(context: Context) {
        vertexArray.init()
        vertexBuffer.init()
        indexBuffer.init()
        circleBuffer.init()
        ballShader.init(
            context = context,
            paths = listOf("ball_vert.glsl", "ball_frag.glsl")
        )
    }

    fun release() {
        vertexArray.release()
        vertexBuffer.release()
        indexBuffer.release()
        circleBuffer.release()
        ballShader.release()
    }

    override fun onSurfaceChanged(
        gl: GL10?,
        width: Int,
        height: Int,
    ) {
        glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(
        gl: GL10?,
        config: EGLConfig?,
    ) {
        glClearColor(0f, 0f, 0f, 1f)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClearColor(0f, 0f, 0f, 1f)
        glClear(GL_COLOR_BUFFER_BIT)

        val scene = this.scene ?: return
        val grid = scene.grid
        val balls = scene.balls
        val projectiles = scene.projectiles
        val circles = scene.circles

        vertexArray.bind()
        ballShader.run()
        circleBuffer.update(circles)
        circleBuffer.flush()
        glDrawElementsInstanced(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0, circles.size)

//        // debug screen
//        STC.drawRect(
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

}