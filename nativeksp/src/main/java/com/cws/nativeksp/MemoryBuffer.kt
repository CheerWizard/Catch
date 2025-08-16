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

    @Synchronized
    fun setDouble(index: Int, value: Double) {
        buffer.putDouble(index, value)
    }

    @Synchronized
    fun getDouble(index: Int) = buffer.getDouble(index)

    @Synchronized
    fun setLong(index: Int, value: Long) {
        buffer.putLong(index, value)
    }

    @Synchronized
    fun getLong(index: Int) = buffer.getLong(index)

    @Synchronized
    fun setFloat(index: Int, value: Float) {
        buffer.putFloat(index, value)
    }

    @Synchronized
    fun getFloat(index: Int) = buffer.getFloat(index)

    @Synchronized
    fun setInt(index: Int, value: Int) {
        buffer.putInt(index, value)
    }

    @Synchronized
    fun getInt(index: Int) = buffer.getInt(index)

    @Synchronized
    fun setShort(index: Int, value: Short) {
        buffer.putShort(index, value)
    }

    @Synchronized
    fun getShort(index: Int) = buffer.getShort(index)

    @Synchronized
    fun setBoolean(index: Int, value: Boolean) {
        buffer.put(index, if (value) 1 else 0)
    }

    @Synchronized
    fun getBoolean(index: Int) = buffer.get(index) == 1.toByte()

    @Synchronized
    fun setVec2(index: Int, value: Vec2) {
        buffer.putLong(index, value.packed)
    }

    @Synchronized
    fun getVec2(index: Int) = Vec2(buffer.getLong(index))

    @Synchronized
    fun setVec3(index: Int, value: Vec3) {
        buffer.putLong(index, value.packed)
    }

    @Synchronized
    fun getVec3(index: Int) = Vec3(buffer.getLong(index))

    @Synchronized
    fun setVec4(index: Int, value: Vec4) {
        buffer.putLong(index, value.packed)
    }

    @Synchronized
    fun getVec4(index: Int) = Vec4(buffer.getLong(index))

    @Synchronized
    fun setColor(index: Int, value: Color) {
        buffer.putInt(index, value.packed)
    }

    @Synchronized
    fun getColor(index: Int) = Color(buffer.getInt(index))

    @Synchronized
    fun copy(src: Int, dest: Int, size: Int) {
        val srcSlice = buffer.duplicate()
        srcSlice.position(src)
        srcSlice.limit(src + size)
        val destSlice = buffer.duplicate()
        destSlice.position(dest)
        destSlice.put(srcSlice)
    }

}