package com.cws.acatch.game

import androidx.compose.runtime.mutableStateOf
import com.cws.acatch.game.collision.CollisionBox
import com.cws.acatch.game.data.GameGrid
import com.cws.acatch.game.data.GameScene
import com.cws.acatch.game.data.ProjectileList
import com.cws.acatch.game.data.Score
import com.cws.acatch.game.data.generateBalls
import com.cws.acatch.game.rendering.BallRenderer
import com.cws.kanvas.sensor.InputSensorManager
import com.cws.kanvas.event.EventListener
import com.cws.kanvas.core.RenderLoop
import com.cws.kanvas.math.Vec2
import com.cws.kanvas.math.Vec4
import com.cws.kmemory.stack

class GameLoop(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    title: String,
    private val inputSensorManager: InputSensorManager
) : RenderLoop(
    x = x,
    y = y,
    width = width,
    height = height,
    title = title
), EventListener {

    var score = mutableStateOf(Score())
    val animateScore = mutableStateOf(false)

    private var scene: GameScene? = null

    private val ballRenderer = BallRenderer()

    override fun onCreate() {
        super.onCreate()
        window.addEventListener(this)
        inputSensorManager.init()
        ballRenderer.init()

        val balls = generateBalls(
            size = 100,
            width = width.toFloat(),
            height = height.toFloat()
        )

        val scene = GameScene(
            screenBox = CollisionBox(
                x = 0f,
                y = 0f,
                w = width.toFloat(),
                h = height.toFloat()
            ),
            grid = GameGrid(
                width = width,
                height = height,
                cellSize = 20
            ),
            balls = balls,
            projectiles = ProjectileList(1),
        )

        this.scene = scene
    }

    override fun onDestroy() {
        ballRenderer.release()
        inputSensorManager.release()
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
        val sensor = inputSensorManager.sensor

        grid.clear()
        grid.fill(
            entities = balls,
            visible = { balls[it].visible == 1 },
            pos = { balls[it].pos },
        )

        stack {
            balls.forEachIndexed { i, ball ->
                if (ball.visible == 1) {
                    val x0 = screenBox.x
                    val x1 = screenBox.x + screenBox.w
                    val y0 = screenBox.y
                    val y1 = screenBox.y + screenBox.h
                    val r = ball.radius
                    ball.velocity += Vec2(sensor.acceleration.x * t, sensor.acceleration.y * t)
                    var x = ball.pos.x
                    var y = ball.pos.y
                    val dx = x + sensor.direction.x * ball.velocity.x * t
                    val dy = y + sensor.direction.y * ball.velocity.y * t
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
                                        return@forEachIndexed
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
    }

    override fun onRender(dt: Float) {
        scene?.let { scene ->
            ballRenderer.render(scene.balls)
        }
    }

    override fun onTapPressed(x: Float, y: Float) {
        spawnProjectile(x, y)
    }

    private fun onProjectileHit(i: Int) {
        val scene = this.scene ?: return
        val balls = scene.balls
        val oldScore = score.value
        val ball = balls[i]
        score.value = score.value.copy(
            value = oldScore.value + 1,
            color = ball.color
        )
        animateScore.value = true
        ball.visible = 0
    }

    private fun onProjectileMissed(i: Int) {}

    private fun spawnProjectile(x: Float, y: Float) {
        val scene = this.scene ?: return
        val projectiles = scene.projectiles
        val y = 2000f
        val i = 0
        stack {
            val projectile = projectiles[i]
            projectile.pos = Vec2(x, y)
            projectile.velocity = Vec2()
            projectile.dir = Vec2(y = -1f)
            projectile.acceleration = Vec2(y = 9.8f * 1000f)
            projectile.length = (64..128).random().toFloat()
            projectile.color = Vec4(0f, 0f, 0f, 1f)
            projectile.visible = true
        }
    }

    private fun destroyProjectile(i: Int) {
        val scene = this.scene ?: return
        scene.projectiles[i].visible = false
    }

}