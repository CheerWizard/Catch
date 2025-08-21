package com.cws.kmemory

expect open class PlatformBuffer(size: Int) {

    val position: Int
    val capacity: Int

    protected fun getBuffer(): Any

    protected open fun resize(newCapacity: Int)

    protected fun setPosition(index: Int)

    fun setDouble(index: Int, value: Double)

    fun getDouble(index: Int): Double

    fun setLong(index: Int, value: Long)

    fun getLong(index: Int): Long

    fun setFloat(index: Int, value: Float)

    fun getFloat(index: Int): Float

    fun setInt(index: Int, value: Int)

    fun setInt(index: Int, value: Boolean)

    fun getInt(index: Int): Int

    fun setShort(index: Int, value: Short)

    fun getShort(index: Int): Short

    fun setBoolean(index: Int, value: Boolean)

    fun getBoolean(index: Int): Boolean

    fun copy(src: Int, dest: Int, size: Int)

    fun copy(destBuffer: PlatformBuffer, src: Int, dest: Int, size: Int)

    fun setArray(value: IntArray)

    fun setArray(value: FloatArray)

}