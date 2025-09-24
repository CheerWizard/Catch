package com.cws.kanvas.sensor

import com.cws.kanvas.math.Vec3

data class InputSensor(
    var acceleration: Vec3 = Vec3(),
    val direction: Vec3 = Vec3()
)