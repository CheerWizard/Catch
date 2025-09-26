package com.cws.fmm

import java.nio.ByteBuffer

actual class BigBuffer actual constructor(capacity: Int) : LockFree(), FastBuffer {

    var buffer: ByteBuffer = CMemory.malloc(capacity)
        ?: throw RuntimeException("Failed to allocate for NativeBuffer $capacity bytes")

    actual override var position: Int
        set(value) {
            buffer.position(value)
        }
        get() = buffer.position()

    actual override val capacity: Int get() = buffer.capacity()

    actual override fun release() {
        CMemory.free(buffer)
    }

    actual override fun getBuffer(): Any = buffer

    actual override fun resize(newCapacity: Int) {
        buffer = CMemory.realloc(buffer, newCapacity)
            ?: throw RuntimeException("Failed to reallocate for NativeBuffer $newCapacity bytes")
    }

    actual override operator fun set(index: Int, value: Byte) {
        buffer.put(index, value)
    }

    actual override operator fun get(index: Int): Byte = buffer.get(index)

    actual override fun copyTo(
        dest: FastBuffer,
        srcIndex: Int,
        destIndex: Int,
        size: Int,
    ) {
        lock {
            if (dest is SmallBuffer) {
                for (i in 0..<size) {
                    dest.smallBuffer[destIndex + i] = buffer.get(srcIndex + i)
                }
            } else if (dest is BigBuffer) {
                val srcSlice = buffer.duplicate()
                srcSlice.position(srcIndex)
                srcSlice.limit(srcIndex + size)
                val destLastPosition = dest.position
                dest.position = destIndex
                dest.buffer.put(srcSlice)
                dest.position = destLastPosition
            }
        }
    }

    actual fun copyFrom(src: SmallBuffer, destIndex: Int, srcIndex: Int, size: Int) {
        lock {
            val oldPosition = position
            position = destIndex
            buffer.put(src.smallBuffer, srcIndex, size)
            position = oldPosition
        }
    }

    actual override fun setBytes(index: Int, bytes: ByteArray) {
        lock {
            buffer.put(bytes, index, bytes.size)
        }
    }

    actual override fun clone(): FastBuffer {
        return BigBuffer(capacity)
    }

}