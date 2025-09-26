package com.cws.fmm

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.get
import kotlinx.cinterop.plus
import kotlinx.cinterop.set
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.usePinned
import platform.posix.free
import platform.posix.malloc
import platform.posix.memcpy
import platform.posix.realloc

@OptIn(ExperimentalForeignApi::class)
actual class BigBuffer actual constructor(capacity: Int) : LockFree(), FastBuffer {

    actual override var position: Int
        set(value) {
            _position = value
        }
        get() = _position

    actual override val capacity: Int get() = _capacity

    var buffer: CPointer<ByteVar> = malloc(capacity.toULong()) as CPointer<ByteVar>
    private var _position = 0
    private var _capacity = capacity

    actual override fun release() {
        free(buffer)
        _position = 0
        _capacity = 0
    }

    actual override fun getBuffer(): Any = buffer

    actual override fun resize(newCapacity: Int) {
        buffer = realloc(buffer, newCapacity.toULong()) as CPointer<ByteVar>
        _capacity = newCapacity
    }

    actual override operator fun set(index: Int, value: Byte) {
        buffer[index] = value
    }

    actual override operator fun get(index: Int): Byte = buffer[index]

    actual override fun copyTo(
        dest: FastBuffer,
        srcIndex: Int,
        destIndex: Int,
        size: Int,
    ) {
        lock {
            val result = if (dest is SmallBuffer) {
                dest.smallBuffer.usePinned { pinned ->
                    memcpy(
                        pinned.addressOf(0) + destIndex,
                        buffer + srcIndex,
                        size.toULong()
                    )
                }
            } else if (dest is BigBuffer) {
                memcpy(
                    dest.buffer + destIndex,
                    buffer + srcIndex,
                    size.toULong()
                )
            } else {}
        }
    }

    actual fun copyFrom(src: SmallBuffer, destIndex: Int, srcIndex: Int, size: Int) {
        lock {
            src.smallBuffer.usePinned { pinned ->
                val result = memcpy(
                    buffer + destIndex,
                     pinned.addressOf(0) + srcIndex,
                    size.toULong()
                )
            }
        }
    }

    actual override fun setBytes(index: Int, bytes: ByteArray) {
        lock {
            val result = memcpy(
                buffer + index,
                bytes.toCValues(),
                bytes.size.toULong()
            )
        }
    }

    actual override fun clone(): FastBuffer {
        return BigBuffer(capacity)
    }

}