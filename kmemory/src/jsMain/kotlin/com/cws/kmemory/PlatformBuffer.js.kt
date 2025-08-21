package com.cws.kmemory

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Float64Array
import org.khronos.webgl.Int16Array
import org.khronos.webgl.Int32Array
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import kotlin.math.roundToLong

actual open class PlatformBuffer actual constructor(size: Int) {

    private var buffer = ArrayBuffer(size)

    private var doubleView = Float64Array(buffer)
    private var floatView = Float32Array(buffer)
    private var intView = Int32Array(buffer)
    private var shortView = Int16Array(buffer)
    private var byteView = Uint8Array(buffer)

    private var _position = 0
    private var _capacity = size

    actual val position: Int = _position
    actual val capacity: Int = _capacity

    protected actual fun getBuffer(): Any = buffer

    protected actual open fun resize(newCapacity: Int) {
        _capacity = newCapacity
        buffer = ArrayBuffer(newCapacity)
        doubleView = Float64Array(buffer)
        floatView = Float32Array(buffer)
        intView = Int32Array(buffer)
        shortView = Int16Array(buffer)
        byteView = Uint8Array(buffer)
    }

    protected actual fun setPosition(index: Int) {
        _position = index
    }

    actual fun setDouble(index: Int, value: Double) {
        doubleView[index] = value
    }

    actual fun getDouble(index: Int): Double = doubleView[index]

    actual fun setLong(index: Int, value: Long) {
        doubleView[index] = value.toDouble()
    }

    actual fun getLong(index: Int): Long = doubleView[index].roundToLong()

    actual fun setFloat(index: Int, value: Float) {
        floatView[index] = value
    }

    actual fun getFloat(index: Int): Float = floatView[index]

    actual fun setInt(index: Int, value: Int) {
        intView[index] = value
    }

    actual fun setInt(index: Int, value: Boolean) {
        intView[index] = if (value) 1 else 0
    }

    actual fun getInt(index: Int): Int = intView[index]

    actual fun setShort(index: Int, value: Short) {
        shortView[index] = value
    }

    actual fun getShort(index: Int): Short = shortView[index]

    actual fun setBoolean(index: Int, value: Boolean) {
        byteView[index] = if (value) 1 else 0
    }

    actual fun getBoolean(index: Int): Boolean = byteView[index].toInt() == 1

    actual fun copy(src: Int, dest: Int, size: Int) {
        byteView.set(byteView.subarray(src, src + size), dest)
    }

    actual fun copy(
        destBuffer: PlatformBuffer,
        src: Int,
        dest: Int,
        size: Int,
    ) {
        destBuffer.byteView.set(byteView.subarray(src, src + size), dest)
    }

    actual fun setArray(value: IntArray) {
        value.forEachIndexed { i, v ->
            intView[i] = v
        }
    }

    actual fun setArray(value: FloatArray) {
        value.forEachIndexed { i, v ->
            floatView[i] = v
        }
    }

}