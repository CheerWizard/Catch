package com.cws.fmm

interface FastBuffer {

    var position: Int
    val capacity: Int

    fun release()
    fun getBuffer(): Any
    fun resize(newCapacity: Int)
    operator fun set(index: Int, value: Byte)
    operator fun get(index: Int): Byte
    fun setBytes(index: Int, bytes: ByteArray)
    fun clone(): FastBuffer
    fun copyTo(dest: FastBuffer, srcIndex: Int, destIndex: Int, size: Int)
    fun copy(srcIndex: Int, destIndex: Int, size: Int) {
        copyTo(this, srcIndex, destIndex, size)
    }

}