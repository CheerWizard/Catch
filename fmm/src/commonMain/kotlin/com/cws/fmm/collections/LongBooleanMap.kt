package com.cws.fmm.collections

import com.cws.fmm.FastCollection

class LongBooleanMap(capacity: Int) : FastCollection {

    private val keys = LongList(capacity)
    private val values = BooleanList(capacity)
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

    operator fun set(key: Long, value: Boolean) {
        val id = (key.hashCode() and Int.MAX_VALUE) % keys.size
        keys[id] = key
        values[id] = value
        used[id] = true
    }

    operator fun get(key: Long, default: Boolean): Boolean {
        val id = (key.hashCode() and Int.MAX_VALUE) % keys.size
        return if (used[id] && keys[id] == key) values[id] else default
    }

    fun contains(key: Long): Boolean {
        val id = (key.hashCode() and Int.MAX_VALUE) % keys.size
        return used[id] && keys[id] == key
    }

}