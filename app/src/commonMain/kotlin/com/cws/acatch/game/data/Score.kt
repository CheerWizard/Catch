package com.cws.acatch.game.data

import com.cws.kanvas.math.Vec4

data class Score(
    var value: Int = 0,
    var color: Vec4 = Vec4(0f, 0f, 0f, 1f)
)