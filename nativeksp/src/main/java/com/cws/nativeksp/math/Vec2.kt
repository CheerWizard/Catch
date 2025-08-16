package com.cws.nativeksp.math

import kotlin.math.sqrt

@JvmInline
value class Vec2(val packed: Long) {

    companion object {
        const val SIZE_BYTES = Long.SIZE_BYTES
    }

    constructor(x: Float, y: Float) : this(pack(x, y))

    val x: Float
        get() = Float.fromBits((packed shr 32).toInt())

    val y: Float
        get() = Float.fromBits(packed.toInt())

    fun setX(x: Float): Vec2 {
        val xBits = x.toBits().toLong() shl 32
        val yBits = packed and 0xFFFF_FFFFL
        return Vec2(xBits or yBits)
    }

    fun setY(y: Float): Vec2 {
        val xBits = packed and (0xFFFF_FFFFL shl 32)
        val yBits = y.toBits().toLong() and 0xFFFF_FFFFL
        return Vec2(xBits or yBits)
    }

    fun length() = sqrt(x * x + y * y)

    fun normalized(): Vec2 {
        val length = length()
        return Vec2(x / length, y / length)
    }

    operator fun component1() = x
    operator fun component2() = y

    operator fun plus(v: Float): Vec2 = Vec2(x + v, y + v)
    operator fun minus(v: Float): Vec2 = Vec2(x - v, y - v)
    operator fun times(v: Float): Vec2 = Vec2(x * v, y * v)
    operator fun div(v: Float): Vec2 = Vec2(x / v, y / v)

    operator fun plus(v: Vec2): Vec2 = Vec2(x + v.x, y + v.y)
    operator fun minus(v: Vec2): Vec2 = Vec2(x - v.x, y - v.y)
    operator fun times(v: Vec2): Vec2 = Vec2(x * v.x, y * v.y)
    operator fun div(v: Vec2): Vec2 = Vec2(x / v.x, y / v.y)

}

internal fun pack(x: Float, y: Float): Long {
    val xBits = x.toBits().toLong() shl 32
    val yBits = y.toBits().toLong() and 0xFFFF_FFFFL
    return xBits or yBits
}

fun dot(v1: Vec2, v2: Vec2): Float {
    return v1.x * v2.x + v1.y * v2.y
}