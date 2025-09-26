package com.cws.fmm.collections

import com.cws.fmm.FastCollection

class ByteLongMap(capacity: Int) : FastCollection {

    private val keys = ByteList(capacity)
    private val values = LongList(capacity)
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

    operator fun set(key: Byte, value: Long) {
        val id = (key.hashCode() and Int.MAX_VALUE) % keys.size
        keys[id] = key
        values[id] = value
        used[id] = true
    }

    operator fun get(key: Byte, default: Long): Long {
        val id = (key.hashCode() and Int.MAX_VALUE) % keys.size
        return if (used[id] && keys[id] == key) values[id] else default
    }

    fun contains(key: Byte): Boolean {
        val id = (key.hashCode() and Int.MAX_VALUE) % keys.size
        return used[id] && keys[id] == key
    }

}