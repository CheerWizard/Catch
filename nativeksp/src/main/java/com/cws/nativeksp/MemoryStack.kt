package com.cws.nativeksp

object MemoryStack : MemoryBuffer(1024 * 1024) {

    @Synchronized
    fun push(size: Int): Int {
        val index = buffer.position()
        val capacity = buffer.capacity()
        if (index == capacity) {
            resize(capacity * 2)
        }
        buffer.position(index + size)
        return index
    }

    @Synchronized
    fun pop(size: Int) {
        val index = buffer.position()
        if (index >= size) {
            buffer.position(index - size)
        }
    }

}