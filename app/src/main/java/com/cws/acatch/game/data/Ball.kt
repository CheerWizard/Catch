package com.cws.acatch.game.data

import com.cws.acatch.game.rendering.CircleData
import com.cws.nativeksp.math.Color
import com.cws.nativeksp.NativeData
import com.cws.nativeksp.math.Vec2

@NativeData(
    autoCreate = true,
    gpuAlignment = true
)
class Ball(
    pos: Vec2,
    visible: Boolean,
    var velocity: Vec2,
    val dir: Vec2,
    val color: Color,
    val radius: Float
) : Entity(pos, visible)

fun generateBalls(size: Int, width: Float, height: Float): BallArray {
    val balls = BallArray(size)
    repeat(size) { i ->
        val color = Color(
            (0..255).random(),
            (0..255).random(),
            (0..255).random(),
            255
        )
        val r = (80..100).random().toFloat()
        balls[i] = BallData.create().apply {
            pos = Vec2(
                (r.toInt()..(width - r).toInt()).random().toFloat(),
                (r.toInt()..(height - r).toInt()).random().toFloat()
            )
            velocity = Vec2(0f, 0f)
            dir = Vec2(
                (-2..2).random().toFloat() / 2f + 0.1f,
                (-2..2).random().toFloat() / 2f + 0.1f
            )
            radius = r
            this.color = color
            visible = true
        }
    }
    return balls
}

fun BallData.toCircleData(index: Int = CircleData.create().index) = CircleData(index).apply {
    this.center = pos
}