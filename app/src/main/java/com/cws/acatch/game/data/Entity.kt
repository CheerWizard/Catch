package com.cws.acatch.game.data

import com.cws.nativeksp.HeapData
import com.cws.nativeksp.math.Vec2

@HeapData
open class Entity(
    var pos: Vec2,
    var visible: Boolean,
)