package com.cws.kmemory

open class NativeBuffer(size: Int) : LockFreeBuffer() {

    internal var buffer = ByteArray(size)

    var position: Int = 0
        protected set

    val capacity: Int = buffer.size

    protected open fun resize(newCapacity: Int) {
        buffer = buffer.copyOf(newCapacity)
    }

    private fun setBits(index: Int, bits: Long, size: Int) {
        repeat(size) { i ->
            buffer[index + i] = ((bits ushr (i * 8)) and 0xFF).toByte()
        }
    }

    private fun getBits(index: Int, size: Int): Long {
        var bits = 0L
        repeat(size) { i ->
            bits = bits or (buffer[index + i].toLong() and 0xFF shl (i * 8))
        }
        return bits
    }

    fun setDouble(index: Int, value: Double) = setBits(index, value.toBits(), Double.SIZE_BYTES)

    fun getDouble(index: Int) = Double.fromBits(getBits(index, Double.SIZE_BYTES))

    fun setLong(index: Int, value: Long) = setBits(index, value, Long.SIZE_BYTES)

    fun getLong(index: Int) = getBits(index, Long.SIZE_BYTES)

    fun setFloat(index: Int, value: Float) = setBits(index, value.toBits().toLong(), Float.SIZE_BYTES)

    fun getFloat(index: Int) = Float.fromBits(getBits(index, Float.SIZE_BYTES).toInt())

    fun setInt(index: Int, value: Int) = setBits(index, value.toLong(), Int.SIZE_BYTES)

    fun setInt(index: Int, value: Boolean) = setBits(index, 1.toLong(), Int.SIZE_BYTES)

    fun getInt(index: Int) = getBits(index, Int.SIZE_BYTES).toInt()

    fun setShort(index: Int, value: Short) = setBits(index, value.toLong(), Short.SIZE_BYTES)

    fun getShort(index: Int) = getBits(index, Short.SIZE_BYTES)

    fun setBoolean(index: Int, value: Boolean) {
        buffer[index] = if (value) 1.toByte() else 0.toByte()
    }

    fun getBoolean(index: Int) = buffer[index] == 1.toByte()

    fun copy(src: Int, dest: Int, size: Int) {
        buffer.copyInto(
            destination = buffer,
            destinationOffset = dest,
            startIndex = src,
            endIndex = src + size
        )
    }

    fun copy(destBuffer: NativeBuffer, src: Int, dest: Int, size: Int) {
        buffer.copyInto(
            destination = destBuffer.buffer,
            destinationOffset = dest,
            startIndex = src,
            endIndex = src + size
        )
    }

    fun setArray(index: Int, value: IntArray) {
        value.forEachIndexed { i, v ->
            setInt(index + i * Int.SIZE_BYTES, v)
        }
    }

    fun setArray(index: Int, value: FloatArray) {
        value.forEachIndexed { i, v ->
            setFloat(index + i * Float.SIZE_BYTES, v)
        }
    }

    fun setArray(index: Int, value: LongArray) {
        value.forEachIndexed { i, v ->
            setLong(index + i * Long.SIZE_BYTES, v)
        }
    }

    fun setArray(index: Int, value: DoubleArray) {
        value.forEachIndexed { i, v ->
            setDouble(index + i * Double.SIZE_BYTES, v)
        }
    }

    fun setArray(index: Int, value: ShortArray) {
        value.forEachIndexed { i, v ->
            setShort(index + i * Short.SIZE_BYTES, v)
        }
    }

    fun setArray(index: Int, value: BooleanArray) {
        value.forEachIndexed { i, v ->
            setBoolean(index + i * Byte.SIZE_BYTES, v)
        }
    }

    fun setArray(index: Int, value: ByteArray) {
        value.copyInto(
            destination = buffer,
            destinationOffset = index
        )
    }

}