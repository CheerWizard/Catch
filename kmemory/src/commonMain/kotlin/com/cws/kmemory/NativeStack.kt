package com.cws.kmemory

object NativeStack : NativeBuffer(1024 * 1024) {

    fun push(size: Int): Int = lock {
        val index = position
        val capacity = capacity
        if (index == capacity) {
            resize(capacity * 2)
        }
        position = index + size
        index
    }

    fun pop(size: Int) {
        lock {
            val index = position
            if (index >= size) {
                position = index - size
            }
        }
    }

}