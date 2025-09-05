package com.cws.kmemory

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set

actual open class BigBuffer actual constructor(capacity: Int) : SmallBuffer(0) {

    override var position: Int
        set(value) {
            _position = value
        }
        get() = _position

    override val capacity: Int get() = buffer.byteLength

    protected var buffer = Uint8Array(capacity)
    protected var _position = 0

    override fun getBuffer(): Any = buffer

    override fun resize(newCapacity: Int) {
        buffer = Uint8Array(newCapacity)
    }

    override operator fun set(index: Int, value: Byte) {
        buffer[index] = value
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
            dest.buffer.set(src.buffer.subarray(srcIndex, srcIndex + size), destIndex)
        }
    }

    override fun setBytes(index: Int, bytes: ByteArray) {
        lock {
            // todo find more efficient way to copy into buffer
            bytes.forEachIndexed { i, byte ->
                buffer[i] = byte
            }
        }
    }

}