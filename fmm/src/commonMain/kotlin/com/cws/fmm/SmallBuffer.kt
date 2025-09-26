package com.cws.fmm

class SmallBuffer(
    capacity: Int
) : LockFree(), FastBuffer {

    override var position: Int
        set(value) {
            _smallPosition = value
        }
        get() = _smallPosition

    override val capacity: Int get() = smallBuffer.size

    var smallBuffer = ByteArray(capacity)
    private var _smallPosition: Int = 0

    override fun release() = Unit

    override fun getBuffer(): Any = smallBuffer

    override fun resize(newCapacity: Int) {
        smallBuffer = smallBuffer.copyOf(newCapacity)
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