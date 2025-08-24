package com.cws.acatch.game.platform

import com.cws.kmemory.NativeData
import com.cws.kmemory.math.Vec3

@NativeData
data class GameSensor(
    val acceleration: Vec3,
    val direction: Vec3
)