package com.cws.nativeksp.math

import kotlin.math.sqrt

@JvmInline
value class Vec3(val packed: Long) {

    companion object {
        const val SIZE_BYTES = Long.SIZE_BYTES
    }

    constructor(x: Float, y: Float, z: Float) : this(pack(x, y, z))

    val x: Float get() = unpack(((packed shr 32) and 0xFFFF).toShort())
    val y: Float get() = unpack(((packed shr 16) and 0xFFFF).toShort())
    val z: Float get() = unpack((packed and 0xFFFF).toShort())

    fun length() = sqrt(x * x + y * y + z * z)

    fun normalized(): Vec3 {
        val length = length()
        return Vec3(x / length, y / length, z / length)
    }

    operator fun component1() = x
    operator fun component2() = y
    operator fun component3() = z

    operator fun plus(v: Float): Vec3 = Vec3(x + v, y + v, z + v)
    operator fun minus(v: Float): Vec3 = Vec3(x - v, y - v, z - v)
    operator fun times(v: Float): Vec3 = Vec3(x * v, y * v, z * v)
    operator fun div(v: Float): Vec3 = Vec3(x / v, y / v, z / v)

    operator fun plus(v: Vec3): Vec3 = Vec3(x + v.x, y + v.y, z + v.z)
    operator fun minus(v: Vec3): Vec3 = Vec3(x - v.x, y - v.y, z - v.z)
    operator fun times(v: Vec3): Vec3 = Vec3(x * v.x, y * v.y, z * v.z)
    operator fun div(v: Vec3): Vec3 = Vec3(x / v.x, y / v.y, z / v.z)

}

internal fun pack(x: Float, y: Float, z: Float): Long {
    val xBits = quantize(x).toLong() and 0xFFFF
    val yBits = quantize(y).toLong() and 0xFFFF
    val zBits = quantize(z).toLong() and 0xFFFF
    return (xBits shl 32) or (yBits shl 16) or zBits
}

internal fun unpack(s: Short): Float {
    return (s.toInt() and 0xFFFF) / 65535f
}

internal fun quantize(v: Float): Short {
    return (v.coerceIn(0f, 1f) * 65535).toInt().toShort()
}

fun dot(v1: Vec3, v2: Vec3): Float {
    return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z
}