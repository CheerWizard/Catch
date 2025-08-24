package com.cws.kmemory

class IntArrayList(capacity: Int) {

    private var items = IntArray(capacity)
    private var position = 0

    val size: Int get() = position
    val capacity: Int get() = items.size

    fun add(item: Int) {
        if (size == capacity) {
            resize(capacity * 2)
        }
        items[position++] = item
    }

    fun remove(i: Int) {
        if (size > 0) {
            position--
        }
    }

    operator fun set(i: Int, item: Int) {
        if (i >= capacity) throw IllegalArgumentException("set($i): Index is out of bounds! $i >= $capacity")
        items[i] = item
    }

    operator fun get(i: Int): Int {
        if (i >= capacity) throw IllegalArgumentException("get($i): Index is out of bounds! $i >= $capacity")
        return items[i]
    }

    private fun resize(newCapacity: Int) {
        items = items.copyOf(newCapacity)
    }

}