package com.cws.kmemory

open class NativeArray(
    private val buffer: SmallBuffer,
    private val typeSize: Int = 1
) : LockFree() {

    var position: Int
        protected set(value) {
            buffer.position = value
        }
        get() = buffer.position

    val capacity: Int = buffer.capacity

    val size: Int get() = position / typeSize

    fun release() {
        buffer.release()
    }

    inline fun <reified T> add(value: T) {
        when (T::class) {
            Byte::class -> addByte(value as Byte)
            Boolean::class -> addByte(if (value as Boolean) 1.toByte() else 0.toByte())
            else -> addBits(value.bits, value.bytes)
        }
    }

    inline operator fun <reified T> set(index: Int, value: T) {
        when (T::class) {
            Byte::class -> setByte(index, value as Byte)
            Boolean::class -> setByte(index, if (value as Boolean) 1.toByte() else 0.toByte())
            else -> setBits(index, value.bits, value.bytes)
        }
    }

    inline operator fun <reified T> get(index: Int): T {
        return when (T::class) {
            Byte::class -> getByte(index) as T
            Boolean::class -> (getByte(index) == 1.toByte()) as T
            Short::class, Int::class, Long::class -> getBits(index, sizeof<T>()) as T
            Float::class -> Float.fromBits(getBits(index, sizeof<T>()).toInt()) as T
            Double::class -> Double.fromBits(getBits(index, sizeof<T>())) as T
            else -> throw UnsupportedOperationException("Unsupported type!")
        }
    }

    inline fun <reified T> setArray(index: Int, value: T) {
        when (T::class) {
            ByteArray::class -> setBytes(index, value as ByteArray)
            BooleanArray::class -> (value as BooleanArray).forEachIndexed { i, v ->
                set(index + i * v.bytes, v)
            }

            ShortArray::class -> (value as ShortArray).forEachIndexed { i, v ->
                set(index + i * v.bytes, v)
            }

            IntArray::class -> (value as IntArray).forEachIndexed { i, v ->
                set(index + i * v.bytes, v)
            }

            LongArray::class -> (value as LongArray).forEachIndexed { i, v ->
                set(index + i * v.bytes, v)
            }

            FloatArray::class -> (value as FloatArray).forEachIndexed { i, v ->
                set(index + i * v.bytes, v)
            }

            DoubleArray::class -> (value as DoubleArray).forEachIndexed { i, v ->
                set(index + i * v.bytes, v)
            }
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

    fun setBytes(index: Int, value: ByteArray) {
        buffer.setBytes(index, value)
    }

    fun copy(src: NativeArray, dest: NativeArray, srcIndex: Int, destIndex: Int, size: Int) {
        buffer.copy(src.buffer, dest.buffer, srcIndex, destIndex, size)
    }

    fun clone(src: Int, dest: Int, size: Int) {
        buffer.clone(src, dest, size)
    }

    fun copyFrom(src: NativeArray, srcIndex: Int, destIndex: Int, size: Int) {
        buffer.copyFrom(src.buffer, srcIndex, destIndex, size)
    }

    fun copyTo(dest: NativeArray, srcIndex: Int, destIndex: Int, size: Int) {
        buffer.copyTo(dest.buffer, srcIndex, destIndex, size)
    }

    fun remove(i: Int) {
        if (size > 0) {
            position -= typeSize
        }
    }

    fun clear() {
        position = 0
    }

    protected fun ensureSize() {
        if (size == this@NativeArray.capacity) {
            resize(this@NativeArray.capacity * 2)
        }
    }

    protected fun ensureCapacity(i: Int) {
        if (i >= this@NativeArray.capacity || i < 0) {
            throw IllegalArgumentException("Index is out of bounds! i=$i capacity=${this@NativeArray.capacity}")
        }
    }

    protected open fun resize(newCapacity: Int) {
        buffer.resize(newCapacity)
    }

}

inline fun <reified T> SmallArray(capacity: Int) = NativeArray(
    buffer = SmallBuffer(capacity),
    typeSize = sizeof<T>()
)

inline fun <reified T> BigArray(capacity: Int) = NativeArray(
    buffer = BigBuffer(capacity),
    typeSize = sizeof<T>()
)