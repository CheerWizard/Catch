package com.cws.fmm

open class FastList(
    capacity: Int,
    val typeSize: Int = Byte.SIZE_BYTES,
    private var requireBigBuffer: Boolean = false
) : FastCollection {

    protected var buffer: FastBuffer = createBuffer(capacity * typeSize)

    var position: Int
        protected set(value) {
            buffer.position = value
        }
        get() = buffer.position

    val capacity: Int = buffer.capacity

    override val size: Int get() = position / typeSize

    override fun release() {
        buffer.release()
    }

    inline val Byte.bits: Long get() = toLong()
    inline val Boolean.bits: Long get() = if (this) 1.toLong() else 0.toLong()
    inline val Short.bits: Long get() = toLong()
    inline val Int.bits: Long get() = toLong()
    inline val Long.bits: Long get() = this
    inline val Float.bits: Long get() = toBits().toLong()
    inline val Double.bits: Long get() = toBits()

    inline val Byte.bytes: Int get() = Byte.SIZE_BYTES
    inline val Boolean.bytes: Int get() = Byte.SIZE_BYTES
    inline val Short.bytes: Int get() = Short.SIZE_BYTES
    inline val Int.bytes: Int get() = Int.SIZE_BYTES
    inline val Long.bytes: Int get() = Long.SIZE_BYTES
    inline val Float.bytes: Int get() = Float.SIZE_BYTES
    inline val Double.bytes: Int get() = Double.SIZE_BYTES

    fun addBoolean(value: Boolean) = addByte(if (value) 1.toByte() else 0.toByte())
    fun addShort(value: Short) = addBits(value.bits, value.bytes)
    fun addInt(value: Int) = addBits(value.bits, value.bytes)
    fun addLong(value: Long) = addBits(value.bits, value.bytes)
    fun addFloat(value: Float) = addBits(value.bits, value.bytes)
    fun addDouble(value: Double) = addBits(value.bits, value.bytes)

    fun setBoolean(index: Int, value: Boolean) = setByte(index, if (value) 1.toByte() else 0.toByte())
    fun setShort(index: Int, value: Short) = setBits(index, value.bits, value.bytes)
    fun setInt(index: Int, value: Int) = setBits(index, value.bits, value.bytes)
    fun setLong(index: Int, value: Long) = setBits(index, value.bits, value.bytes)
    fun setFloat(index: Int, value: Float) = setBits(index, value.bits, value.bytes)
    fun setDouble(index: Int, value: Double) = setBits(index, value.bits, value.bytes)

    fun getBoolean(index: Int): Boolean = getByte(index) == 1.toByte()
    fun getShort(index: Int): Short = getBits(index, Short.SIZE_BYTES).toShort()
    fun getInt(index: Int): Int = getBits(index, Int.SIZE_BYTES).toInt()
    fun getLong(index: Int): Long = getBits(index, Long.SIZE_BYTES)
    fun getFloat(index: Int): Float = Float.fromBits(getBits(index, Float.SIZE_BYTES).toInt())
    fun getDouble(index: Int): Double = Double.fromBits(getBits(index, Double.SIZE_BYTES))

    protected fun addFastObject(handle: MemoryHandle) {
        setFastObject(position, handle)
        position += typeSize
    }

    protected fun setFastObject(index: Int, handle: MemoryHandle) {
        HeapMemory.copyTo(this, handle, index, typeSize)
    }

    inline fun <reified T> addArray(value: T) {
        when (T::class) {
            ByteArray::class -> addBytes(value as ByteArray)
            BooleanArray::class -> (value as BooleanArray).forEach { addBoolean(it) }
            ShortArray::class -> (value as ShortArray).forEach { addShort(it) }
            IntArray::class -> (value as IntArray).forEach { addInt(it) }
            LongArray::class -> (value as LongArray).forEach { addLong(it) }
            FloatArray::class -> (value as FloatArray).forEach { addFloat(it) }
            DoubleArray::class -> (value as DoubleArray).forEach { addDouble(it) }
            else -> throw UnsupportedOperationException("Unsupported type!")
        }
    }

    inline fun <reified T> setArray(index: Int, value: T) {
        when (T::class) {
            ByteArray::class -> setBytes(index, value as ByteArray)

            BooleanArray::class -> (value as BooleanArray).forEachIndexed { i, v ->
                setBoolean(index + i * v.bytes, v)
            }

            ShortArray::class -> (value as ShortArray).forEachIndexed { i, v ->
                setShort(index + i * v.bytes, v)
            }

            IntArray::class -> (value as IntArray).forEachIndexed { i, v ->
                setInt(index + i * v.bytes, v)
            }

            LongArray::class -> (value as LongArray).forEachIndexed { i, v ->
                setLong(index + i * v.bytes, v)
            }

            FloatArray::class -> (value as FloatArray).forEachIndexed { i, v ->
                setFloat(index + i * v.bytes, v)
            }

            DoubleArray::class -> (value as DoubleArray).forEachIndexed { i, v ->
                setDouble(index + i * v.bytes, v)
            }

            else -> throw UnsupportedOperationException("Unsupported type!")
        }
    }

    fun addByte(byte: Byte) {
        ensureSize()
        buffer[position++] = byte
    }

    fun setByte(index: Int, value: Byte) {
        ensureCapacity(index)
        buffer[index] = value
    }

    fun getByte(index: Int): Byte {
        ensureCapacity(index)
        return buffer[index]
    }

    fun addBits(bits: Long, size: Int) {
        ensureSize()
        setBits(position, bits, size)
        position += size
    }

    fun setBits(index: Int, bits: Long, size: Int) {
        ensureCapacity(index)
        repeat(size) { i ->
            buffer[index + i] = ((bits ushr (i * 8)) and 0xFF).toByte()
        }
    }

    fun getBits(index: Int, size: Int): Long {
        ensureCapacity(index)
        var bits = 0L
        repeat(size) { i ->
            bits = bits or (buffer[index + i].toLong() and 0xFF shl (i * 8))
        }
        return bits
    }

    fun addBytes(value: ByteArray) {
        setBytes(position, value)
        position += value.size
    }

    fun setBytes(index: Int, value: ByteArray) {
        buffer.setBytes(index, value)
    }

    fun copy(src: Int, dest: Int, size: Int) {
        buffer.copy(src, dest, size)
    }

    fun copyTo(dest: FastList, srcIndex: Int, destIndex: Int, size: Int) {
        buffer.copyTo(dest.buffer, srcIndex, destIndex, size)
    }

    fun remove(i: Int) {
        if (size > 0) {
            position -= typeSize
        }
    }

    override fun clear() {
        position = 0
    }

    protected fun ensureSize() {
        if (size == this@FastList.capacity) {
            resize(this@FastList.capacity * 2)
        }
    }

    protected fun ensureCapacity(i: Int) {
        if (i >= this@FastList.capacity || i < 0) {
            throw IllegalArgumentException("Index is out of bounds! i=$i capacity=${this@FastList.capacity}")
        }
    }

    protected open fun resize(newCapacity: Int): FastBuffer {
        if (requireBigBuffer) {
            buffer.resize(newCapacity)
            return buffer
        }

        try {
            if (buffer.capacity == 0) {
                buffer = SmallBuffer(newCapacity)
            } else if (buffer is BigBuffer) {
                buffer.release()
                buffer = SmallBuffer(newCapacity)
            } else {
                buffer.resize(newCapacity)
            }
        } catch (_: Throwable) {
            // catching JVM out of memory
            buffer = BigBuffer(newCapacity)
        }

        return buffer
    }

    private fun createBuffer(capacity: Int): FastBuffer {
        return if (requireBigBuffer) BigBuffer(capacity) else SmallBuffer(capacity)
    }

}