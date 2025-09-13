package com.cws.acatch.game.data

import com.cws.acatch.game.collision.CollisionBox

class GameScene(
    val screenBox: CollisionBox,
    val grid: GameGrid,
    val balls: BallList,
    val projectiles: ProjectileList
)