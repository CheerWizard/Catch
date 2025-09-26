package com.cws.acatch.game.data

import com.cws.kanvas.math.Vec2
import com.cws.fmm.FastObject

@FastObject
open class _Entity(
    var pos: Vec2,
    var visible: Boolean,
)