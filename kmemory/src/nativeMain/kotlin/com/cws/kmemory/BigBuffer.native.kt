package com.cws.kmemory

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.plus
import kotlinx.cinterop.set
import kotlinx.cinterop.toCValues
import platform.posix.free
import platform.posix.malloc
import platform.posix.memcpy
import platform.posix.realloc

@OptIn(ExperimentalForeignApi::class)
actual open class BigBuffer actual constructor(capacity: Int) : SmallBuffer(0) {

    override var position: Int
        set(value) {
            _position = value
        }
        get() = _position

    override val capacity: Int get() = _capacity

    private var buffer: CPointer<ByteVar> = malloc(capacity.toULong()) as CPointer<ByteVar>
    private var _position = 0
    private var _capacity = capacity

    override fun release() {
        free(buffer)
        _position = 0
        _capacity = 0
    }

    override fun getBuffer(): Any = buffer

    override fun resize(newCapacity: Int) {
        buffer = realloc(buffer, newCapacity.toULong()) as CPointer<ByteVar>
        _capacity = newCapacity
    }

    override operator fun set(index: Int, value: Byte) {
        buffer[index] = value
    }

    override operator fun get(index: Int): Byte = buffer[index]

    override fun copy(
        src: SmallBuffer,
        dest: SmallBuffer,
        srcIndex: Int,
        destIndex: Int,
        size: Int
    ) {
        lock {
            src as BigBuffer
            dest as BigBuffer
            memcpy(dest.buffer + destIndex, src.buffer + srcIndex, size.toULong())
        }
    }

    override fun setBytes(index: Int, bytes: ByteArray) {
        lock {
            memcpy(buffer + index, bytes.toCValues(), bytes.size.toULong())
        }
    }

}