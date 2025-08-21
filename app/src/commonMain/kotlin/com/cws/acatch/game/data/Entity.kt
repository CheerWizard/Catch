package com.cws.acatch.game.data

import com.cws.kmemory.NativeData
import com.cws.kmemory.math.Vec2

@NativeData
open class Entity(
    var pos: Vec2,
    var visible: Boolean,
)