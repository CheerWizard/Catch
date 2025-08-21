package com.cws.acatch.game

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope
import com.cws.acatch.game.collision.CollisionBox
import com.cws.acatch.game.data.EntityData
import com.cws.acatch.game.data.GameGrid
import com.cws.acatch.game.data.GameScene
import com.cws.acatch.game.data.ProjectileArray
import com.cws.acatch.game.data.Score
import com.cws.acatch.game.data.generateBalls
import com.cws.acatch.game.data.toCircleData
import com.cws.acatch.game.data.*
import com.cws.acatch.game.rendering.CircleArray
import com.cws.acatch.game.rendering.GameRenderer
import com.cws.kmemory.math.Color
import com.cws.kmemory.math.Vec2
import timber.log.Timber
import kotlin.collections.forEachIndexed
import kotlin.collections.get
import kotlin.math.abs

class GameManager(
    private val width: Float,
    private val height: Float,
    private val context: Context,
    val renderer: GameRenderer
) : SensorEventListener {

    var score = mutableStateOf(Score())
    val animateScore = mutableStateOf(false)

    private val tag = GameManager::class.java.simpleName
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var prevTime = 0L
    private var dt = 0L

    private var sensorAX = 0f
    private var sensorAY = 0f
    private var sensorAZ = 0f
    private var sensorDX = 0f
    private var sensorDY = 0f
    private var sensorDZ = 0f

    private var scene: GameScene? = null

    fun onCreate(time: Long) {
        prevTime = time
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        renderer.init(context)

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
        renderer.setScene(scene)
    }

    fun onDestroy() {
        sensorManager.unregisterListener(this)
        renderer.release()
    }

    fun onUpdate(time: Long) {
        dt = time - prevTime
        prevTime = time
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
                                    onTapInside(j)
                                    destroyProjectile(i)
                                    return
                                }
                                r < l -> {
                                    onTapOutside(j)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun onTap(position: Offset) {
        spawnProjectile(position)
    }

    private fun onTapInside(i: Int) {
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

    private fun onTapOutside(i: Int) {
    }

    private fun spawnProjectile(position: Offset) {
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

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        val x = event.values[0] * 10f
        val y = event.values[1] * 10f
        val z = event.values[2] * 10f

        Timber.d("onSensorChanged: $sensorAX:$sensorAY:$sensorAZ")

        sensorAX = abs(x)
        sensorAY = abs(y)
        sensorAZ = abs(z)

        sensorDX = if (x > 3f) -1f else 1f
        sensorDY = if (y > 3f) 1f else -1f
        sensorDZ = if (z > 3f) -1f else 1f
    }

}

suspend fun PointerInputScope.handleInput(gameManager: GameManager) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            when (event.type) {
                PointerEventType.Press -> {
                    val position = event.changes.first().position
                    gameManager.onTap(position)
                }
            }
        }
    }
}