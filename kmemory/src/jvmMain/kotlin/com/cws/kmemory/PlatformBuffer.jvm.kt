package com.cws.kmemory

import java.nio.ByteBuffer
import java.nio.ByteOrder

actual open class PlatformBuffer actual constructor(size: Int) {

    protected var buffer = ByteBuffer
        .allocateDirect(size)
        .order(ByteOrder.nativeOrder())

    actual val position: Int = buffer.position()
    actual val capacity: Int = buffer.capacity()

    private var intView = buffer.asIntBuffer()
    private var floatView = buffer.asFloatBuffer()

    protected actual fun getBuffer(): Any = buffer

    protected actual fun setPosition(index: Int) {
        buffer.position(index)
    }

    protected actual open fun resize(newCapacity: Int) {
        val newBuffer = ByteBuffer
            .allocateDirect(newCapacity)
            .order(buffer.order())
        buffer.flip()
        newBuffer.put(buffer)
        buffer = newBuffer
        intView = buffer.asIntBuffer()
        floatView = buffer.asFloatBuffer()
    }

    actual fun setDouble(index: Int, value: Double) {
        buffer.putDouble(index, value)
    }

    actual fun getDouble(index: Int) = buffer.getDouble(index)

    actual fun setLong(index: Int, value: Long) {
        buffer.putLong(index, value)
    }

    actual fun getLong(index: Int) = buffer.getLong(index)

    actual fun setFloat(index: Int, value: Float) {
        buffer.putFloat(index, value)
    }

    actual fun getFloat(index: Int) = buffer.getFloat(index)

    actual fun setInt(index: Int, value: Int) {
        buffer.putInt(index, value)
    }

    actual fun setInt(index: Int, value: Boolean) {
        buffer.putInt(index, if (value) 1 else 0)
    }

    actual fun getInt(index: Int) = buffer.getInt(index)

    actual fun setShort(index: Int, value: Short) {
        buffer.putShort(index, value)
    }

    actual fun getShort(index: Int) = buffer.getShort(index)

    actual fun setBoolean(index: Int, value: Boolean) {
        buffer.put(index, if (value) 1 else 0)
    }

    actual fun getBoolean(index: Int) = buffer.get(index) == 1.toByte()

    actual fun copy(src: Int, dest: Int, size: Int) {
        val srcSlice = buffer.duplicate()
        srcSlice.position(src)
        srcSlice.limit(src + size)
        val destSlice = buffer.duplicate()
        destSlice.position(dest)
        destSlice.put(srcSlice)
    }

    actual fun copy(destBuffer: PlatformBuffer, src: Int, dest: Int, size: Int) {
        val srcSlice = buffer.duplicate()
        srcSlice.position(src)
        srcSlice.limit(src + size)
        destBuffer.setPosition(dest)
        destBuffer.buffer.put(srcSlice)
    }

    actual fun setArray(value: IntArray) {
        intView.put(value)
    }

    actual fun setArray(value: FloatArray) {
        floatView.put(value)
    }

}