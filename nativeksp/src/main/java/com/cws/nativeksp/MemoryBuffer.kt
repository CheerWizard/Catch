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

    protected fun resize(newCapacity: Int) {
        val newBuffer = ByteBuffer
            .allocateDirect(newCapacity)
            .order(buffer.order())
        buffer.clear()
        newBuffer.put(buffer)
        newBuffer.flip()
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
        buffer.putLong(index, value.packed)
    }

    fun getVec2(index: Int) = Vec2(buffer.getLong(index))

    fun setVec3(index: Int, value: Vec3) {
        buffer.putLong(index, value.packed)
    }

    fun getVec3(index: Int) = Vec3(buffer.getLong(index))

    fun setVec4(index: Int, value: Vec4) {
        buffer.putLong(index, value.packed)
    }

    fun getVec4(index: Int) = Vec4(buffer.getLong(index))

    fun setColor(index: Int, value: Color) {
        buffer.putInt(index, value.packed)
    }

    fun getColor(index: Int) = Color(buffer.getInt(index))

    fun copy(src: Int, dest: Int, size: Int) {
        val srcSlice = buffer.duplicate()
        srcSlice.position(src)
        srcSlice.limit(src + size)
        val destSlice = buffer.duplicate()
        destSlice.position(dest)
        destSlice.put(srcSlice)
    }

}