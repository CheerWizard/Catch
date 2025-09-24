package com.cws.acatch.game.rendering

import com.cws.acatch.game.data.Ball
import com.cws.acatch.game.data.BallList
import com.cws.kanvas.pipeline.IndexBuffer
import com.cws.kanvas.core.Kanvas
import com.cws.kanvas.pipeline.Shader
import com.cws.kanvas.shader.ShaderManager
import com.cws.kanvas.pipeline.UniformBuffer
import com.cws.kanvas.pipeline.VERTEX_ATTRIBUTES
import com.cws.kanvas.pipeline.VertexArray
import com.cws.kanvas.pipeline.VertexBuffer

class BallBuffer : UniformBuffer(
    binding = 0,
    typeSize = Ball.SIZE_BYTES,
    count = 100,
)

class BallRenderer {

    private val vertexArray = VertexArray(VERTEX_ATTRIBUTES)
    private val vertexBuffer = VertexBuffer(4)
    private val indexBuffer = IndexBuffer(6)
    private val shader = Shader()
    private val ballBuffer = BallBuffer()

    fun init() {
        ShaderManager.load("ball.vert", "ball.frag") {
            shader.init(it)
        }
        vertexArray.init()
        vertexBuffer.init()
        indexBuffer.init()
        ballBuffer.init()
    }

    fun release() {
        vertexArray.release()
        vertexBuffer.release()
        indexBuffer.release()
        shader.release()
        ballBuffer.release()
    }

    fun render(balls: BallList) {
        if (shader.isReady()) {
            vertexArray.bind()
            ballBuffer.bind()
            shader.run()
            Kanvas.run {
                drawElementsInstanced(TRIANGLES, 6, UINT, 0, balls.size)
            }
        }
    }

}