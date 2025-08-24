package com.cws.acatch.game

import com.cws.acatch.game.collision.CollisionBox
import com.cws.acatch.game.data.*
import com.cws.acatch.game.data.EntityData
import com.cws.acatch.game.data.ProjectileArray
import com.cws.acatch.game.platform.GameSensorManager
import com.cws.acatch.game.rendering.CircleArray
import com.cws.kanvas.EventListener
import com.cws.kanvas.RenderLoop
import com.cws.kmemory.math.Color
import com.cws.kmemory.math.Vec2
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.collections.forEachIndexed
import kotlin.collections.get
import kotlin.collections.map
import kotlin.collections.minus
import kotlin.collections.plus
import kotlin.collections.plusAssign
import kotlin.compareTo
import kotlin.map
import kotlin.plus
import kotlin.repeat
import kotlin.sequences.map
import kotlin.sequences.minus
import kotlin.sequences.plus
import kotlin.text.compareTo
import kotlin.text.map
import kotlin.text.plus
import kotlin.text.set

class GameLoop(
    private val width: Float,
    private val height: Float,
    private val gameSensorManager: GameSensorManager
) : RenderLoop(), EventListener {

    var score = Score()
    val animateScore = false

    private var scene: GameScene? = null

    override fun onCreate() {
        super.onCreate()
        window.addEventListener(this)
        gameSensorManager.init()

        val balls = generateBalls(
            size = 100,
            width = width,
            height = height
        )

        val circles = CircleArray(100).create()
        repeat(circles.size) { i ->
            circles[i] = balls[i].toCircleData(circles[i].index)
        }

        val scene = GameScene(
            screenBox = CollisionBox(
                x = 0f,
                y = 0f,
                w = width,
                h = height
            ),
            grid = GameGrid(
                width = width.toInt(),
                height = height.toInt(),
                cellSize = 20
            ),
            balls = balls,
            projectiles = ProjectileArray(1),
            circles = circles
        )

        this.scene = scene
    }

    override fun onDestroy() {
        gameSensorManager.release()
        window.removeEventListener(this)
        super.onDestroy()
    }

    override fun onFrameUpdate(dt: Float) {
        val t = dt / 1000f

        val scene = this.scene ?: return

        val screenBox = scene.screenBox
        val balls = scene.balls
        val projectiles = scene.projectiles
        val grid = scene.grid

        grid.clear()
        grid.fill(balls.map { EntityData(it.index) })

        balls.forEachIndexed { i, ball ->
            if (ball.visible) {
                val x0 = screenBox.x
                val x1 = screenBox.x + screenBox.w
                val y0 = screenBox.y
                val y1 = screenBox.y + screenBox.h
                val r = ball.radius
                ball.velocity += Vec2(sensorAX * t, sensorAY * t)
                val dx = ball.pos.x + sensorDX * ball.velocity.x * t
                val dy = ball.pos.y + sensorDY * ball.velocity.y * t

                var x = ball.pos.x
                var y = ball.pos.y
                if (dx - r > x0 && dx + r < x1) x = dx
                if (dy - r > y0 && dy + r < y1) y = dy
                ball.pos = Vec2(x, y)
            }
        }

        projectiles.forEachIndexed { i, projectile ->
            if (projectile.visible) {
                projectile.velocity += projectile.acceleration * t
                projectile.pos += projectile.dir * projectile.velocity * t

                val px = projectile.pos.x
                val py = projectile.pos.y
                val col = grid.col(px)
                val row = grid.row(py)

                for (dy in -1..1) {
                    for (dx in -1..1) {
                        val c = (col + dx).coerceIn(0, grid.cols - 1)
                        val r = (row + dy).coerceIn(0, grid.rows - 1)
                        val cellIndex = r * grid.cols + c
                        val cellBalls = grid.cells[cellIndex]
                        repeat(cellBalls.size) { j ->
                            val ball = balls[j]
                            val dx = px - ball.pos.x
                            val dy = py - ball.pos.y
                            val r = ball.radius * ball.radius
                            val l = dx * dx + dy * dy
                            when {
                                r >= l -> {
                                    onProjectileHit(j)
                                    destroyProjectile(i)
                                    return
                                }
                                r < l -> {
                                    onProjectileMissed(j)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onRender(dt: Float) {}

    override fun onTapPressed(x: Float, y: Float) {
        spawnProjectile(x, y)
    }

    private fun onProjectileHit(i: Int) {
        val scene = this.scene ?: return
        val balls = scene.balls
        val oldScore = score.value
        score.value = score.value.copy(
            value = oldScore.value + 1,
            color = balls[i].color
        )
        animateScore.value = true
        balls[i].visible = false
    }

    private fun onProjectileMissed(i: Int) {}

    private fun spawnProjectile(x: Float, y: Float) {
        val scene = this.scene ?: return
        val projectiles = scene.projectiles
        val x = position.x
        val y = 2000f
        val i = 0
        val projectile = projectiles[i]
        projectile.pos = Vec2(x, y)
        projectile.velocity = Vec2(0f, 0f)
        projectile.dir = Vec2(0f, -1f)
        projectile.acceleration = Vec2(0f, 9.8f * 1000f)
        projectile.length = (64..128).random().toFloat()
        projectile.color = Color.Black
        projectile.visible = true
    }

    private fun destroyProjectile(i: Int) {
        val scene = this.scene ?: return
        scene.projectiles[i].visible = false
    }

}