package com.cws.kmemory

import com.cws.kmemory.math.Color
import com.cws.kmemory.math.Vec2
import com.cws.kmemory.math.Vec3
import com.cws.kmemory.math.Vec4
import kotlinx.atomicfu.atomic

open class NativeBuffer(size: Int) : PlatformNativeBuffer(size) {

    private val lock = atomic(false)

    protected fun <T> lock(block: () -> T): T {
        while (!lock.compareAndSet(expect = false, update = true)) {}
        val result = block()
        lock.value = false
        return result
    }

    fun setVec2(index: Int, value: Vec2) {
        setFloat(index, value.x)
        setFloat(index + Float.SIZE_BYTES, value.y)
    }

    fun getVec2(index: Int): Vec2 {
        return Vec2(
            getFloat(index),
            getFloat(index * Float.SIZE_BYTES)
        )
    }

    fun setVec3(index: Int, value: Vec3) {
        setFloat(index, value.x)
        setFloat(index + Float.SIZE_BYTES, value.y)
        setFloat(index + Float.SIZE_BYTES * 2, value.z)
    }

    fun getVec3(index: Int): Vec3 {
        return Vec3(
            getFloat(index),
            getFloat(index * Float.SIZE_BYTES),
            getFloat(index * Float.SIZE_BYTES * 2),
        )
    }

    fun setVec4(index: Int, value: Vec4) {
        setFloat(index, value.x)
        setFloat(index + Float.SIZE_BYTES, value.y)
        setFloat(index + Float.SIZE_BYTES * 2, value.z)
        setFloat(index + Float.SIZE_BYTES * 3, value.w)
    }

    fun getVec4(index: Int): Vec4 {
        return Vec4(
            getFloat(index),
            getFloat(index * Float.SIZE_BYTES),
            getFloat(index * Float.SIZE_BYTES * 2),
            getFloat(index * Float.SIZE_BYTES * 3),
        )
    }

    fun setColor(index: Int, value: Color) {
        setInt(index, value.r)
        setInt(index + Int.SIZE_BYTES, value.g)
        setInt(index + Int.SIZE_BYTES * 2, value.b)
        setInt(index + Int.SIZE_BYTES * 3, value.a)
    }

    fun getColor(index: Int): Color {
        return Color(
            getInt(index),
            getInt(index + Int.SIZE_BYTES),
            getInt(index + Int.SIZE_BYTES * 2),
            getInt(index + Int.SIZE_BYTES * 3),
        )
    }

}