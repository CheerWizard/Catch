package com.cws.acatch.game.rendering

import com.cws.acatch.graphics.UniformBuffer
import com.cws.kmemory.NativeData
import com.cws.kmemory.math.Vec2
import com.cws.kmemory.math.Vec4

@NativeData
data class Circle(
    val center: Vec2,
    val radius: Float,
    val smoothness: Float,
    val color: Vec4,
)

class CircleBuffer : UniformBuffer<CircleData>(
    binding = 0,
    structSize = CircleData.SIZE_BYTES,
    structCount = 100
)