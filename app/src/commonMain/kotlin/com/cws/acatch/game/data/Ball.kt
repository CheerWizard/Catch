package com.cws.acatch.game.data

import com.cws.kanvas.math.Vec2
import com.cws.kanvas.math.Vec4
import com.cws.kmemory.FastObject
import com.cws.kmemory.stack

@FastObject(
    gpuAlignment = true
)
class _Ball(
    pos: Vec2,
    visible: Boolean,
    var velocity: Vec2,
    val dir: Vec2,
    val color: Vec4,
    val radius: Float
) : _Entity(pos, visible)

fun generateBalls(size: Int, width: Float, height: Float): BallList {
    val balls = BallList(size)
    stack {
        val r = (80..100).random().toFloat()
        repeat(size) { i ->
            balls[i] = Ball(
                pos = Vec2(
                    (r.toInt()..(width - r).toInt()).random().toFloat(),
                    (r.toInt()..(height - r).toInt()).random().toFloat()
                ),
                velocity = Vec2(0f, 0f),
                dir = Vec2(
                    (-2..2).random().toFloat() / 2f + 0.1f,
                    (-2..2).random().toFloat() / 2f + 0.1f
                ),
                radius = r,
                color = Vec4(
                    (0..255).random() / 255f,
                    (0..255).random() / 255f,
                    (0..255).random() / 255f,
                    1f
                ),
                visible = 1
            )
        }
    }
    return balls
}