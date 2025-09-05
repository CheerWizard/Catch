package com.cws.acatch.game.data

import com.cws.kmemory.NativeData
import com.cws.kmemory.math.Vec2
import com.cws.kmemory.math.Vec4

@NativeData
class Projectile(
    pos: Vec2,
    visible: Boolean,
    var velocity: Vec2,
    var dir: Vec2,
    var length: Float,
    var acceleration: Vec2,
    var color: Vec4
) : Entity(pos, visible)