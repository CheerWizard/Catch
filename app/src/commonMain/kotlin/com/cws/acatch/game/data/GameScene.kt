package com.cws.acatch.game.data

import com.cws.acatch.game.collision.CollisionBox
import com.cws.acatch.game.rendering.CircleArray

class GameScene(
    val screenBox: CollisionBox,
    val grid: GameGrid,
    val balls: BallArray,
    val projectiles: ProjectileArray,
    val circles: CircleArray
)