package com.cws.kanvas.input

import com.cws.kmemory.math.Vec3

data class InputSensor(
    var acceleration: Vec3 = Vec3(),
    val direction: Vec3 = Vec3()
)