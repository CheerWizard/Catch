package com.cws.kmemory.math

import com.cws.kmemory.NativeArray
import com.cws.kmemory.NativeHeap
import com.cws.kmemory.NativeStack
import kotlin.jvm.JvmInline
import kotlin.math.sqrt

@JvmInline
value class Vec4(val index: Int) {

    constructor(
        x: Float = 0f,
        y: Float = 0f,
        z: Float = 0f,
        w: Float = 0f,
        index: Int = create().index
    ) : this(index) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    var x: Float
        get() = NativeHeap[index + Float.SIZE_BYTES * 0]
        set(value) {
            NativeHeap[index + Float.SIZE_BYTES * 0] = value
        }

    var y: Float
        get() = NativeHeap[index + Float.SIZE_BYTES * 1]
        set(value) {
            NativeHeap[index + Float.SIZE_BYTES * 1] = value
        }

    var z: Float
        get() = NativeHeap[index + Float.SIZE_BYTES * 2]
        set(value) {
            NativeHeap[index + Float.SIZE_BYTES * 2] = value
        }

    var w: Float
        get() = NativeHeap[index + Float.SIZE_BYTES * 3]
        set(value) {
            NativeHeap[index + Float.SIZE_BYTES * 3] = value
        }

    fun free() {
        NativeHeap.free(index, SIZE_BYTES)
    }

    fun length(): Float {
        val x = x
        val y = y
        val z = z
        val w = w
        return sqrt(x * x + y * y + z * z + w * w)
    }

    fun normalized(): Vec4 {
        val length = length()
        if (length == 0f) return Vec4()
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

    companion object {
        const val SIZE_BYTES = Float.SIZE_BYTES * 4
        fun create(): Vec4 = Vec4(NativeHeap.allocate(SIZE_BYTES))
    }

}

fun NativeStack.Vec4(x: Float = 0f, y: Float = 0f, z: Float = 0f, w: Float = 0f): Vec4 {
    return Vec4(x, y, z, w, push(Vec4.SIZE_BYTES))
}

fun NativeArray.add(value: Vec4) {
    add(value.x)
    add(value.y)
    add(value.z)
    add(value.w)
}

fun dot(v1: Vec4, v2: Vec4): Float {
    return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z + v1.w * v2.w
}