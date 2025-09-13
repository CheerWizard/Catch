package com.cws.kanvas.math

import com.cws.kmemory.HeapMemory
import com.cws.kmemory.NULL
import com.cws.kmemory.StackMemory
import com.cws.kmemory.checkNotNull
import kotlin.jvm.JvmInline
import kotlin.math.sqrt

// Used to generate fresh version
//@FastObject
//class _Vec2(
//    var x: Float,
//    var y: Float
//)

@JvmInline
value class Vec2(
    val index: Int,
) {
    var x: Float
        get() {
            index.checkNotNull()
            return HeapMemory.getFloat(index)
        }
        set(`value`) {
            index.checkNotNull()
            HeapMemory.setFloat(index, value)
        }

    var y: Float
        get() {
            index.checkNotNull()
            return HeapMemory.getFloat(index + Float.SIZE_BYTES)
        }
        set(`value`) {
            index.checkNotNull()
            HeapMemory.setFloat(index + Float.SIZE_BYTES, value)
        }

    constructor(
        x: Float = 0f,
        y: Float = 0f,
        index: Int = create().index,
    ) : this(index) {
        this.x = x
        this.y = y
    }

    fun free(): Vec2 {
        HeapMemory.free(index, SIZE_BYTES)
        return Vec2(NULL)
    }

    fun copy(): Vec2 = Vec2(
        x = x,
        y = y,)

    companion object {
        const val SIZE_BYTES: Int = Float.SIZE_BYTES + Float.SIZE_BYTES

        fun create(): Vec2 = Vec2(HeapMemory.allocate(SIZE_BYTES))
    }

    fun length(): Float {
        val x = x
        val y = y
        return sqrt(x * x + y * y)
    }

    fun normalized(): Vec2 {
        val length = length()
        if (length == 0f) return Vec2()
        return Vec2(x / length, y / length)
    }

    fun dot(v: Vec2): Float {
        return x * v.x + y * v.y
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

fun StackMemory.Vec2(x: Float = 0f, y: Float = 0f): Vec2 = Vec2(x,y, push(Vec2.SIZE_BYTES))

inline fun <T> Vec2.use(block: (`value`: Vec2) -> T): T {
    try {
        return block(this)
    } finally {
        free()
    }
}