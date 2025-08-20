package com.cws.acatch.game.data

import com.cws.nativeksp.math.Color
import com.cws.nativeksp.NativeData
import com.cws.nativeksp.math.Vec2

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