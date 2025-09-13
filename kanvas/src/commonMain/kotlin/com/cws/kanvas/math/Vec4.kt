package com.cws.kanvas.math

import com.cws.kmemory.FastList
import com.cws.kmemory.FastObject
import com.cws.kmemory.HeapMemory
import com.cws.kmemory.NULL
import com.cws.kmemory.StackMemory
import com.cws.kmemory.checkNotNull
import kotlin.jvm.JvmInline
import kotlin.math.sqrt

// Used to generate fresh version
//@FastObject
//class _Vec4(
//    var x: Float,
//    var y: Float,
//    var z: Float,
//    var w: Float
//)

@JvmInline
value class Vec4(
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

    var z: Float
        get() {
            index.checkNotNull()
            return HeapMemory.getFloat(index + Float.SIZE_BYTES + Float.SIZE_BYTES)
        }
        set(`value`) {
            index.checkNotNull()
            HeapMemory.setFloat(index + Float.SIZE_BYTES + Float.SIZE_BYTES, value)
        }

    var w: Float
        get() {
            index.checkNotNull()
            return HeapMemory.getFloat(index + Float.SIZE_BYTES + Float.SIZE_BYTES + Float.SIZE_BYTES)
        }
        set(`value`) {
            index.checkNotNull()
            HeapMemory.setFloat(index + Float.SIZE_BYTES + Float.SIZE_BYTES + Float.SIZE_BYTES, value)
        }

    constructor(
        x: Float = 0f,
        y: Float = 0f,
        z: Float = 0f,
        w: Float = 0f,
        index: Int = create().index,
    ) : this(index) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    fun free(): Vec4 {
        HeapMemory.free(index, SIZE_BYTES)
        return Vec4(NULL)
    }

    fun copy(): Vec4 = Vec4(
        x = x,
        y = y,
        z = z,
        w = w,)

    companion object {
        const val SIZE_BYTES: Int = Float.SIZE_BYTES + Float.SIZE_BYTES + Float.SIZE_BYTES +
                Float.SIZE_BYTES

        fun create(): Vec4 = Vec4(HeapMemory.allocate(SIZE_BYTES))
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

    fun dot(v: Vec4): Float {
        return x * v.x + y * v.y + z * v.z + w * v.w
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

fun StackMemory.Vec4(
    x: Float = 0f,
    y: Float = 0f,
    z: Float = 0f,
    w: Float = 0f,
): Vec4 = Vec4(x,y,z,w, push(Vec4.SIZE_BYTES))

inline fun <T> Vec4.use(block: (`value`: Vec4) -> T): T {
    try {
        return block(this)
    } finally {
        free()
    }
}