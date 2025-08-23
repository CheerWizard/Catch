package com.cws.kmemory

import kotlinx.cinterop.BooleanVar
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.DoubleVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.LongVar
import kotlinx.cinterop.ShortVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.free
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.plus
import kotlinx.cinterop.pointed
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.value
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual open class PlatformBuffer actual constructor(size: Int) : LockFreeBuffer() {

    private var buffer: CPointer<ByteVar> = nativeHeap.allocArray(size.toLong())

    private var _position = 0
    private var _capacity = size

    actual val position: Int = _position
    actual val capacity: Int = _capacity

    protected actual fun getBuffer(): Any = buffer

    protected actual open fun resize(newCapacity: Int) {
        _capacity = newCapacity
        nativeHeap.free(buffer)
        buffer = nativeHeap.allocArray(newCapacity)
    }

    protected actual fun setPosition(index: Int) {
        _position = index
    }

    actual fun setDouble(index: Int, value: Double) {
        val ptr = (buffer + index)?.reinterpret<DoubleVar>() ?: return
        ptr.pointed.value = value
    }

    actual fun getDouble(index: Int): Double {
        val ptr = (buffer + index)?.reinterpret<DoubleVar>() ?: return 0.0
        return ptr.pointed.value
    }

    actual fun setLong(index: Int, value: Long) {
        val ptr = (buffer + index)?.reinterpret<LongVar>() ?: return
        ptr.pointed.value = value
    }

    actual fun getLong(index: Int): Long {
        val ptr = (buffer + index)?.reinterpret<LongVar>() ?: return 0
        return ptr.pointed.value
    }

    actual fun setFloat(index: Int, value: Float) {
        val ptr = (buffer + index)?.reinterpret<FloatVar>() ?: return
        ptr.pointed.value = value
    }

    actual fun getFloat(index: Int): Float {
        val ptr = (buffer + index)?.reinterpret<FloatVar>() ?: return 0f
        return ptr.pointed.value
    }

    actual fun setInt(index: Int, value: Int) {
        val ptr = (buffer + index)?.reinterpret<IntVar>() ?: return
        ptr.pointed.value = value
    }

    actual fun setInt(index: Int, value: Boolean) {
        val ptr = (buffer + index)?.reinterpret<IntVar>() ?: return
        ptr.pointed.value = if (value) 1 else 0
    }

    actual fun getInt(index: Int): Int {
        val ptr = (buffer + index)?.reinterpret<IntVar>() ?: return 0
        return ptr.pointed.value
    }

    actual fun setShort(index: Int, value: Short) {
        val ptr = (buffer + index)?.reinterpret<ShortVar>() ?: return
        ptr.pointed.value = value
    }

    actual fun getShort(index: Int): Short {
        val ptr = (buffer + index)?.reinterpret<ShortVar>() ?: return 0
        return ptr.pointed.value
    }

    actual fun setBoolean(index: Int, value: Boolean) {
        val ptr = (buffer + index)?.reinterpret<BooleanVar>() ?: return
        ptr.pointed.value = value
    }

    actual fun getBoolean(index: Int): Boolean {
        val ptr = (buffer + index)?.reinterpret<BooleanVar>() ?: return false
        return ptr.pointed.value
    }

    actual fun copy(src: Int, dest: Int, size: Int) {
        memcpy(buffer + dest, buffer + src, size.toULong())
    }

    actual fun copy(
        destBuffer: PlatformBuffer,
        src: Int,
        dest: Int,
        size: Int,
    ) {
        memcpy(destBuffer.buffer + dest, buffer + src, size.toULong())
    }

    actual fun setArray(value: IntArray) {
        // TODO toCValues is slow here
        memcpy(buffer, value.toCValues(), (value.size * Int.SIZE_BYTES).toULong())
    }

    actual fun setArray(value: FloatArray) {
        // TODO toCValues is slow here
        memcpy(buffer, value.toCValues(), (value.size * Float.SIZE_BYTES).toULong())
    }

}