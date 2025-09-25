package com.cws.kmemory

class SmallBuffer(
    capacity: Long
) : LockFree(), FastBuffer {

    override var position: Long
        set(value) {
            _smallPosition = value
        }
        get() = _smallPosition

    override val capacity: Long get() = smallBuffer.size.toLong()

    var smallBuffer = ByteArray(capacity.toInt())
    private var _smallPosition: Long = 0

    override fun release() = Unit

    override fun getBuffer(): Any = smallBuffer

    override fun resize(newCapacity: Long) {
        smallBuffer = smallBuffer.copyOf(newCapacity.toInt())
    }

    override operator fun set(index: Int, value: Byte) {
        smallBuffer[index] = value
    }

    override operator fun get(index: Int): Byte = smallBuffer[index]

    override fun copyTo(dest: FastBuffer, srcIndex: Int, destIndex: Int, size: Int) {
        lock {
            if (dest is SmallBuffer) {
                smallBuffer.copyInto(
                    destination = dest.smallBuffer,
                    destinationOffset = destIndex,
                    startIndex = srcIndex,
                    endIndex = srcIndex + size
                )
            } else if (dest is BigBuffer) {
                dest.copyFrom(this, destIndex, srcIndex, size)
            }
        }
    }

    override fun setBytes(index: Int, bytes: ByteArray) {
        lock {
            bytes.copyInto(destination = smallBuffer, destinationOffset = index)
        }
    }

    override fun clone(): FastBuffer {
        return SmallBuffer(capacity)
    }

}