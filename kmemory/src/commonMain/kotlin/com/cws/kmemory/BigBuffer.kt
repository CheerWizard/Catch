package com.cws.kmemory

expect class BigBuffer(capacity: Long) : FastBuffer, LockFree {
    override var position: Long
    override val capacity: Long
    override fun release()
    override fun getBuffer(): Any
    override fun resize(newCapacity: Int)
    override fun set(index: Int, value: Byte)
    override fun get(index: Int): Byte
    override fun setBytes(index: Int, bytes: ByteArray)
    override fun clone(): FastBuffer
    override fun copyTo(
        dest: FastBuffer,
        srcIndex: Int,
        destIndex: Int,
        size: Int,
    )
    fun copyFrom(src: SmallBuffer, destIndex: Int, srcIndex: Int, size: Int)
}