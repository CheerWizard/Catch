package com.cws.acatch.game.data

import com.cws.kanvas.math.Vec2
import com.cws.kanvas.math.Vec4
import com.cws.kmemory.FastObject

@FastObject
class _Projectile(
    pos: Vec2,
    visible: Boolean,
    var velocity: Vec2,
    var dir: Vec2,
    var length: Float,
    var acceleration: Vec2,
    var color: Vec4
) : _Entity(pos, visible)