package com.cws.kmemory.math

import com.cws.kmemory.NativeHeap
import com.cws.kmemory.NativeStack
import kotlin.jvm.JvmInline
import kotlin.math.sqrt

@JvmInline
value class Vec2(val index: Int) {

    constructor(
        x: Float = 0f,
        y: Float = 0f,
        index: Int = create().index
    ) : this(index) {
        this.x = x
        this.y = y
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

    fun free() {
        NativeHeap.free(index, SIZE_BYTES)
    }

    fun length(): Float {
        val x = x
        val y = y
        return sqrt(x * x + y * y)
    }

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

    companion object {
        const val SIZE_BYTES = Float.SIZE_BYTES * 2
        private fun create(): Vec2 = Vec2(NativeHeap.allocate(SIZE_BYTES))
    }

}

fun NativeStack.Vec2(x: Float = 0f, y: Float = 0f): Vec2 {
    return Vec2(x, y, push(Vec2.SIZE_BYTES))
}

fun dot(v1: Vec2, v2: Vec2): Float {
    return v1.x * v2.x + v1.y * v2.y
}