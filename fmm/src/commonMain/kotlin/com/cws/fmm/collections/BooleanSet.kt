package com.cws.fmm.collections

import com.cws.fmm.FastCollection

class BooleanSet(
    capacity: Int
) : FastCollection {

    private val values = BooleanList(capacity)
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

    fun add(value: Boolean): Boolean {
        val id = (value.hashCode() and Int.MAX_VALUE) % values.size
        if (used[id] && values[id] == value) return false
        values[id] = value
        used[id] = true
        return true
    }

    fun contains(value: Boolean): Boolean {
        val id = (value.hashCode() and Int.MAX_VALUE) % values.size
        return used[id] && values[id] == value
    }

}