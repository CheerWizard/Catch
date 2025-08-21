package com.cws.acatch.game.data

import com.cws.kmemory.NativeData
import com.cws.kmemory.math.Color
import com.cws.kmemory.math.Vec2

@NativeData
class Projectile(
    pos: Vec2,
    visible: Boolean,
    var velocity: Vec2,
    var dir: Vec2,
    var length: Float,
    var acceleration: Vec2,
    var color: Color
) : Entity(pos, visible)