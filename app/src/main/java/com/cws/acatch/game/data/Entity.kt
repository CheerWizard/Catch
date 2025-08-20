package com.cws.acatch.game.data

import com.cws.nativeksp.NativeData
import com.cws.nativeksp.math.Vec2

@NativeData
open class Entity(
    var pos: Vec2,
    var visible: Boolean,
)