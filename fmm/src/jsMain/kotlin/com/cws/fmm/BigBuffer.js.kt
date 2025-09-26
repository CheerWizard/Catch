package com.cws.fmm

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set

actual class BigBuffer actual constructor(capacity: Int) : LockFree(), FastBuffer {

    actual override var position: Int
        set(value) {
            _position = value
        }
        get() = _position

    actual override val capacity: Int get() = buffer.byteLength

    var buffer = Uint8Array(capacity)
    protected var _position: Int = 0

    actual override fun getBuffer(): Any = buffer

    actual override fun resize(newCapacity: Int) {
        buffer = Uint8Array(newCapacity)
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