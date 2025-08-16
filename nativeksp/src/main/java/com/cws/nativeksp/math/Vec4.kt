package com.cws.nativeksp.math

import kotlin.math.sqrt

@JvmInline
value class Vec4(val packed: Long) {

    companion object {
        const val SIZE_BYTES = Long.SIZE_BYTES
    }

    constructor(x: Float, y: Float, z: Float, w: Float) : this(pack(x, y, z, w))

    val x: Float get() = unpack(((packed shr 48) and 0xFFFF).toShort())
    val y: Float get() = unpack(((packed shr 32) and 0xFFFF).toShort())
    val z: Float get() = unpack(((packed shr 16) and 0xFFFF).toShort())
    val w: Float get() = unpack((packed and 0xFFFF).toShort())

    fun length() = sqrt(x * x + y * y + z * z + w * w)

    fun normalized(): Vec4 {
        val length = length()
        return Vec4(x / length, y / length, z / length, w / length)
    }

    operator fun component1() = x
    operator fun component2() = y
    operator fun component3() = z
    operator fun component4() = w

    operator fun plus(v: Float): Vec4 = Vec4(x + v, y + v, z + v, w + v)
    operator fun minus(v: Float): Vec4 = Vec4(x - v, y - v, z - v, w - v)
    operator fun times(v: Float): Vec4 = Vec4(x * v, y * v, z * v, w * v)
    operator fun div(v: Float): Vec4 = Vec4(x / v, y / v, z / v, w / v)

    operator fun plus(v: Vec4): Vec4 = Vec4(x + v.x, y + v.y, z + v.z, w + v.w)
    operator fun minus(v: Vec4): Vec4 = Vec4(x - v.x, y - v.y, z - v.z, w - v.w)
    operator fun times(v: Vec4): Vec4 = Vec4(x * v.x, y * v.y, z * v.z, w * v.w)
    operator fun div(v: Vec4): Vec4 = Vec4(x / v.x, y / v.y, z / v.z, w / v.w)

}

internal fun pack(x: Float, y: Float, z: Float, w: Float): Long {
    val xBits = quantize(x).toLong() and 0xFFFF
    val yBits = quantize(y).toLong() and 0xFFFF
    val zBits = quantize(z).toLong() and 0xFFFF
    val wBits = quantize(w).toLong() and 0xFFFF
    return (xBits shl 48) or (yBits shl 32) or (zBits shl 16) or wBits
}

fun dot(v1: Vec4, v2: Vec4): Float {
    return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z + v1.w * v2.w
}