package com.cws.acatch.game.data

import com.cws.nativeksp.math.Color
import com.cws.nativeksp.HeapData
import com.cws.nativeksp.math.Vec2

@HeapData
class Projectile(
    pos: Vec2,
    visible: Boolean,
    var velocity: Vec2,
    var dir: Vec2,
    var length: Float,
    var acceleration: Vec2,
    var color: Color
) : Entity(pos, visible)