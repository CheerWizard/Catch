package com.cws.kmemory

interface FastBuffer {

    var position: Long
    val capacity: Long

    fun release()
    fun getBuffer(): Any
    fun resize(newCapacity: Long)
    operator fun set(index: Int, value: Byte)
    operator fun get(index: Int): Byte
    fun setBytes(index: Int, bytes: ByteArray)
    fun clone(): FastBuffer
    fun copyTo(dest: FastBuffer, srcIndex: Int, destIndex: Int, size: Int)
    fun copy(srcIndex: Int, destIndex: Int, size: Int) {
        copyTo(this, srcIndex, destIndex, size)
    }

}