package com.cws.kmemory

open class SmallBuffer(
    capacity: Int
) : LockFree() {

    open var position: Int
        set(value) {
            _smallPosition = value
        }
        get() = _smallPosition

    open val capacity: Int get() = smallBuffer.size

    private var smallBuffer = ByteArray(capacity)
    private var _smallPosition = 0

    open fun release() = Unit

    protected open fun getBuffer(): Any = smallBuffer

    open fun resize(newCapacity: Int) {
        smallBuffer = smallBuffer.copyOf(newCapacity)
    }

    open operator fun set(index: Int, value: Byte) {
        smallBuffer[index] = value
    }

    open operator fun get(index: Int): Byte = smallBuffer[index]

    open fun copy(src: SmallBuffer, dest: SmallBuffer, srcIndex: Int, destIndex: Int, size: Int) {
        lock {
            src.smallBuffer.copyInto(
                destination = dest.smallBuffer,
                destinationOffset = destIndex,
                startIndex = srcIndex,
                endIndex = srcIndex + size
            )
        }
    }

    open fun setBytes(index: Int, bytes: ByteArray) {
        lock {
            bytes.copyInto(destination = smallBuffer, destinationOffset = index)
        }
    }

}

fun SmallBuffer.copyTo(dest: SmallBuffer, srcIndex: Int, destIndex: Int, size: Int) {
    copy(this, dest, srcIndex, destIndex, size)
}

fun SmallBuffer.copyFrom(src: SmallBuffer, srcIndex: Int, destIndex: Int, size: Int) {
    copy(src, this, srcIndex, destIndex, size)
}

fun SmallBuffer.clone(srcIndex: Int, destIndex: Int, size: Int) {
    copy(this, this, srcIndex, destIndex, size)
}