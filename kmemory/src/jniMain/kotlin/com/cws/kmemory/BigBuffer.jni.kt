package com.cws.kmemory

import java.nio.ByteBuffer

actual open class BigBuffer actual constructor(capacity: Int) : SmallBuffer(0) {

    protected var buffer: ByteBuffer = Memory.nativeAlloc(capacity)
        ?: throw RuntimeException("Failed to allocate for NativeBuffer $capacity bytes")

    override var position: Int
        set(value) {
            buffer.position(value)
        }
        get() = buffer.position()

    override val capacity: Int get() = buffer.capacity()

    override fun release() {
        Memory.nativeFree(buffer)
    }

    override fun getBuffer(): Any = buffer

    override fun resize(newCapacity: Int) {
        buffer = Memory.nativeRealloc(buffer, newCapacity)
            ?: throw RuntimeException("Failed to reallocate for NativeBuffer $newCapacity bytes")
    }

    override operator fun set(index: Int, value: Byte) {
        buffer.put(index, value)
    }

    override operator fun get(index: Int): Byte = buffer.get(index)

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
            val srcSlice = src.buffer.duplicate()
            srcSlice.position(srcIndex)
            srcSlice.limit(srcIndex + size)
            val destLastPosition = dest.position
            dest.position = destIndex
            dest.buffer.put(srcSlice)
            dest.position = destLastPosition
        }
    }

    override fun setBytes(index: Int, bytes: ByteArray) {
        lock {
            buffer.put(bytes, index, bytes.size)
        }
    }

}