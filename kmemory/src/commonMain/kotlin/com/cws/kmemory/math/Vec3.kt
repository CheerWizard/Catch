package com.cws.kmemory.math

import com.cws.kmemory.NativeHeap
import kotlin.jvm.JvmInline
import kotlin.math.sqrt

@JvmInline
value class Vec3(val index: Int) {

    constructor(x: Float, y: Float, z: Float) : this(create().index) {
        this.x = x
        this.y = y
        this.z = z
    }

    var x: Float
        get() = NativeHeap.getFloat(index + Float.SIZE_BYTES * 0)
        set(value) {
            NativeHeap.setFloat(index + Float.SIZE_BYTES * 0, value)
        }

    var y: Float
        get() = NativeHeap.getFloat(index + Float.SIZE_BYTES * 1)
        set(value) {
            NativeHeap.setFloat(index + Float.SIZE_BYTES * 1, value)
        }

    var z: Float
        get() = NativeHeap.getFloat(index + Float.SIZE_BYTES * 2)
        set(value) {
            NativeHeap.setFloat(index + Float.SIZE_BYTES * 2, value)
        }

    fun free() {
        NativeHeap.free(index, SIZE_BYTES)
    }

    fun length(): Float {
        val x = x
        val y = y
        val z = z
        return sqrt(x * x + y * y + z * z)
    }

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

    companion object {
        const val SIZE_BYTES = Float.SIZE_BYTES * 3
        fun create(): Vec3 = Vec3(NativeHeap.allocate(SIZE_BYTES))
    }

}

fun dot(v1: Vec3, v2: Vec3): Float {
    return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z
}