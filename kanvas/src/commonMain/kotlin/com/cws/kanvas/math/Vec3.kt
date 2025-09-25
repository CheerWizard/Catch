package com.cws.kanvas.math

import com.cws.kmemory.HeapMemory
import com.cws.kmemory.MemoryHandle
import com.cws.kmemory.NULL
import com.cws.kmemory.StackMemory
import com.cws.kmemory.checkNotNull
import kotlin.jvm.JvmInline
import kotlin.math.sqrt

// Used to generate fresh version
//@FastObject
//class _Vec3(
//    var x: Float,
//    var y: Float,
//    var z: Float
//)

@JvmInline
value class Vec3(
    val handle: MemoryHandle,
) {
    var x: Float
        get() {
            handle.checkNotNull()
            return HeapMemory.getFloat(handle)
        }
        set(`value`) {
            handle.checkNotNull()
            HeapMemory.setFloat(handle, value)
        }

    var y: Float
        get() {
            handle.checkNotNull()
            return HeapMemory.getFloat(handle + Float.SIZE_BYTES)
        }
        set(`value`) {
            handle.checkNotNull()
            HeapMemory.setFloat(handle + Float.SIZE_BYTES, value)
        }

    var z: Float
        get() {
            handle.checkNotNull()
            return HeapMemory.getFloat(handle + Float.SIZE_BYTES + Float.SIZE_BYTES)
        }
        set(`value`) {
            handle.checkNotNull()
            HeapMemory.setFloat(handle + Float.SIZE_BYTES + Float.SIZE_BYTES, value)
        }

    constructor(
        x: Float = 0f,
        y: Float = 0f,
        z: Float = 0f,
        index: Int = create().handle,
    ) : this(index) {
        this.x = x
        this.y = y
        this.z = z
    }

    fun free(): Vec3 {
        HeapMemory.free(handle, SIZE_BYTES)
        return Vec3(NULL)
    }

    fun copy(): Vec3 = Vec3(
        x = x,
        y = y,
        z = z,)

    companion object {
        const val SIZE_BYTES: Int = Float.SIZE_BYTES + Float.SIZE_BYTES + Float.SIZE_BYTES

        fun create(): Vec3 = Vec3(HeapMemory.allocate(SIZE_BYTES))
    }

    fun length(): Float {
        val x = x
        val y = y
        val z = z
        return sqrt(x * x + y * y + z * z)
    }

    fun normalized(): Vec3 {
        val length = length()
        if (length == 0f) return Vec3()
        return Vec3(x / length, y / length, z / length)
    }

    fun dot(v: Vec3): Float {
        return x * v.x + y * v.y + z * v.z
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

fun StackMemory.Vec3(
    x: Float = 0f,
    y: Float = 0f,
    z: Float = 0f,
): Vec3 = Vec3(x,y,z, push(Vec3.SIZE_BYTES))

inline fun <T> Vec3.use(block: (`value`: Vec3) -> T): T {
    try {
        return block(this)
    } finally {
        free()
    }
}