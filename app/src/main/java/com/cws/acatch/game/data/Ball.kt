package com.cws.acatch.game.data

import com.cws.nativeksp.math.Color
import com.cws.nativeksp.HeapData
import com.cws.nativeksp.math.Mat2
import com.cws.nativeksp.math.Mat4
import com.cws.nativeksp.math.Vec2

@HeapData
class Ball(
    pos: Vec2,
    visible: Boolean,
    var velocity: Vec2,
    val dir: Vec2,
    val color: Color,
    val radius: Float,
    val scale: Mat4,
    val rotation: Mat4,
    val view: Mat2,
    val projectile: Projectile
) : Entity(pos, visible)

fun generateBalls(size: Int, width: Float, height: Float): ArrayList<BallData> {
    val balls = ArrayList<BallData>(size)
    repeat(size) { i ->
        val color = Color(
            (0..255).random(),
            (0..255).random(),
            (0..255).random(),
            255
        )
        val r = (80..100).random().toFloat()
        balls.add(BallData.create().apply {
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
        })
    }
    return balls
}