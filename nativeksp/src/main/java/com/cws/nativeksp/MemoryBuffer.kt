package com.cws.nativeksp

import com.cws.nativeksp.math.Color
import com.cws.nativeksp.math.Vec2
import com.cws.nativeksp.math.Vec3
import com.cws.nativeksp.math.Vec4
import java.nio.ByteBuffer
import java.nio.ByteOrder

open class MemoryBuffer(size: Int) {

    protected var buffer: ByteBuffer = ByteBuffer
        .allocateDirect(size)
        .order(ByteOrder.nativeOrder())

    protected open fun resize(newCapacity: Int) {
        val newBuffer = ByteBuffer
            .allocateDirect(newCapacity)
            .order(buffer.order())
        buffer.flip()
        newBuffer.put(buffer)
        buffer = newBuffer
    }

    fun setDouble(index: Int, value: Double) {
        buffer.putDouble(index, value)
    }

    fun getDouble(index: Int) = buffer.getDouble(index)

    fun setLong(index: Int, value: Long) {
        buffer.putLong(index, value)
    }

    fun getLong(index: Int) = buffer.getLong(index)

    fun setFloat(index: Int, value: Float) {
        buffer.putFloat(index, value)
    }

    fun getFloat(index: Int) = buffer.getFloat(index)

    fun setInt(index: Int, value: Int) {
        buffer.putInt(index, value)
    }

    fun setInt(index: Int, value: Boolean) {
        buffer.putInt(index, if (value) 1 else 0)
    }

    fun getInt(index: Int) = buffer.getInt(index)

    fun setShort(index: Int, value: Short) {
        buffer.putShort(index, value)
    }

    fun getShort(index: Int) = buffer.getShort(index)

    fun setBoolean(index: Int, value: Boolean) {
        buffer.put(index, if (value) 1 else 0)
    }

    fun getBoolean(index: Int) = buffer.get(index) == 1.toByte()

    fun setVec2(index: Int, value: Vec2) {
        buffer.putFloat(index, value.x)
        buffer.putFloat(index + Float.SIZE_BYTES, value.y)
    }

    fun getVec2(index: Int): Vec2 {
        return Vec2(
            buffer.getFloat(index),
            buffer.getFloat(index + Float.SIZE_BYTES),
        )
    }

    fun setVec3(index: Int, value: Vec3) {
        buffer.putFloat(index, value.x)
        buffer.putFloat(index + Float.SIZE_BYTES, value.y)
        buffer.putFloat(index + Float.SIZE_BYTES * 2, value.z)
    }

    fun getVec3(index: Int): Vec3 {
        return Vec3(
            buffer.getFloat(index),
            buffer.getFloat(index + Float.SIZE_BYTES),
            buffer.getFloat(index + Float.SIZE_BYTES * 2),
        )
    }

    fun setVec4(index: Int, value: Vec4) {
        buffer.putFloat(index, value.x)
        buffer.putFloat(index + Float.SIZE_BYTES, value.y)
        buffer.putFloat(index + Float.SIZE_BYTES * 2, value.z)
        buffer.putFloat(index + Float.SIZE_BYTES * 3, value.w)
    }

    fun getVec4(index: Int): Vec4 {
        return Vec4(
            buffer.getFloat(index),
            buffer.getFloat(index + Float.SIZE_BYTES),
            buffer.getFloat(index + Float.SIZE_BYTES * 2),
            buffer.getFloat(index + Float.SIZE_BYTES * 3),
        )
    }

    fun setColor(index: Int, value: Color) {
        buffer.putInt(index, value.r)
        buffer.putInt(index + Int.SIZE_BYTES, value.g)
        buffer.putInt(index + Int.SIZE_BYTES * 2, value.b)
        buffer.putInt(index + Int.SIZE_BYTES * 3, value.a)
    }

    fun getColor(index: Int): Color {
        return Color(
            buffer.getInt(index),
            buffer.getInt(index + Int.SIZE_BYTES),
            buffer.getInt(index + Int.SIZE_BYTES * 2),
            buffer.getInt(index + Int.SIZE_BYTES * 3),
        )
    }

    fun copy(src: Int, dest: Int, size: Int) {
        val srcSlice = buffer.duplicate()
        srcSlice.position(src)
        srcSlice.limit(src + size)
        val destSlice = buffer.duplicate()
        destSlice.position(dest)
        destSlice.put(srcSlice)
    }

    fun copy(destBuffer: ByteBuffer, src: Int, dest: Int, size: Int) {
        val srcSlice = buffer.duplicate()
        srcSlice.position(src)
        srcSlice.limit(src + size)
        destBuffer.position(dest)
        destBuffer.put(srcSlice)
    }

}