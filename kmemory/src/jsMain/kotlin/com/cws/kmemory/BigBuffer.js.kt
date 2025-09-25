package com.cws.kmemory

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set

// TODO: probably will need to move JS BigBuffer to C++/WASM memory buffer
//  current implementation is as limited as SmallBuffer by size
actual class BigBuffer actual constructor(capacity: Long) : LockFree(), FastBuffer {

    actual override var position: Long
        set(value) {
            _position = value
        }
        get() = _position

    actual override val capacity: Long get() = buffer.byteLength.toLong()

    var buffer = Uint8Array(capacity.toInt())
    protected var _position: Long = 0

    actual override fun getBuffer(): Any = buffer

    actual override fun resize(newCapacity: Long) {
        buffer = Uint8Array(newCapacity.toInt())
    }

    actual override operator fun set(index: Int, value: Byte) {
        buffer[index] = value
    }

    actual override operator fun get(index: Int): Byte = buffer.get(index)

    actual override fun setBytes(index: Int, bytes: ByteArray) {
        lock {
            buffer.set(bytes.toTypedArray())
        }
    }

    actual override fun clone(): FastBuffer {
        return BigBuffer(capacity)
    }

    actual override fun release() = Unit

    actual override fun copyTo(
        dest: FastBuffer,
        srcIndex: Int,
        destIndex: Int,
        size: Int,
    ) {
        lock {
            if (dest is SmallBuffer) {
                for (i in 0..<size) {
                    dest.smallBuffer[destIndex + i] = buffer[srcIndex + i]
                }
            } else if (dest is BigBuffer) {
                dest.buffer.set(buffer.subarray(srcIndex, srcIndex + size), destIndex)
            }
        }
    }

    actual fun copyFrom(
        src: SmallBuffer,
        destIndex: Int,
        srcIndex: Int,
        size: Int
    ) {
        lock {
            for (i in 0..<size) {
                buffer[destIndex + i] = src.smallBuffer[srcIndex + i]
            }
        }
    }

}