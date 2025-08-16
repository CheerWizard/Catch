package com.cws.acatch.game

import android.content.Context
import android.opengl.GLES30.*
import android.opengl.GLSurfaceView
import com.cws.acatch.game.data.Ball
import com.cws.acatch.game.data.GameScene
import com.cws.acatch.graphics.IndexBuffer
import com.cws.acatch.graphics.Shader
import com.cws.acatch.graphics.Vertex
import com.cws.acatch.graphics.VertexArray
import com.cws.acatch.graphics.VertexBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GameRenderer(
    private val ballShader: Shader = Shader(),
    private val projectileShader: Shader = Shader(),
    private val gridShader: Shader = Shader(),
    private val vertexArray: VertexArray = VertexArray(Vertex.ATTRIBUTES),
    private val vertexBuffer: VertexBuffer = VertexBuffer(4),
    private val indexBuffer: IndexBuffer = IndexBuffer(6),
) : GLSurfaceView.Renderer {

    private var scene: GameScene? = null

    fun setScene(scene: GameScene) {
        this.scene = scene
    }

    fun init(context: Context) {
        vertexArray.init()
        vertexBuffer.init()
        indexBuffer.init()

        ballShader.init(
            context = context,
            paths = listOf("ball_vert.glsl", "ball_frag.glsl")
        )
    }

    fun release() {
        vertexArray.release()
        vertexBuffer.release()
        indexBuffer.release()
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
        glClear(GL_COLOR_BUFFER_BIT)

        val scene = this.scene ?: return

        val grid = scene.grid
        val balls = scene.balls
        val projectiles = scene.projectiles

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

    private fun drawBalls(balls: ArrayList<Ball>) {
//        repeat(balls.x.size) { i ->
//            if (balls.visible[i]) {
//                val center = Offset(balls.x[i], balls.y[i])
//                val radius = balls.r[i]
//                drawCircle(
//                    center = center,
//                    brush = Brush.radialGradient(
//                        colors = listOf(Color.White, Color(balls.color[i])),
//                        center = center,
//                        radius = radius
//                    ),
//                    radius = radius
//                )
//            }
//        }
    }

}