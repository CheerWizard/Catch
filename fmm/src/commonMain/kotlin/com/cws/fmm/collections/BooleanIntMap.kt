package com.cws.fmm.collections

import com.cws.fmm.FastCollection

class BooleanIntMap(capacity: Int) : FastCollection {

    private val keys = BooleanList(capacity)
    private val values = IntList(capacity)
    private val used = BooleanList(capacity)

    override val size: Int = keys.size

    override fun release() {
        keys.release()
        values.release()
        used.release()
    }

    override fun clear() {
        keys.clear()
        values.clear()
        used.clear()
    }

    operator fun set(key: Boolean, value: Int) {
        val id = (key.hashCode() and Int.MAX_VALUE) % keys.size
        keys[id] = key
        values[id] = value
        used[id] = true
    }

    operator fun get(key: Boolean, default: Int): Int {
        val id = (key.hashCode() and Int.MAX_VALUE) % keys.size
        return if (used[id] && keys[id] == key) values[id] else default
    }

    fun contains(key: Boolean): Boolean {
        val id = (key.hashCode() and Int.MAX_VALUE) % keys.size
        return used[id] && keys[id] == key
    }

}