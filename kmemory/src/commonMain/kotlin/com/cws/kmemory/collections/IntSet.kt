package com.cws.kmemory.collections

import com.cws.kmemory.FastCollection

class IntSet(
    capacity: Int
) : FastCollection {

    private val values = IntList(capacity)
    private val used = BooleanList(capacity)

    override val size: Int = values.size

    override fun release() {
        values.release()
        used.release()
    }

    override fun clear() {
        values.clear()
        used.clear()
    }

    fun add(value: Int): Boolean {
        val id = (value.hashCode() and Int.MAX_VALUE) % values.size
        if (used[id] && values[id] == value) return false
        values[id] = value
        used[id] = true
        return true
    }

    fun contains(value: Int): Boolean {
        val id = (value.hashCode() and Int.MAX_VALUE) % values.size
        return used[id] && values[id] == value
    }

}