package com.cws.fmm.collections

import com.cws.fmm.FastList

class LongList(capacity: Int) : FastList(capacity, Long.SIZE_BYTES) {

    fun clone(): LongList = LongList(capacity)

    fun add(value: Long) = addLong(value)

    operator fun set(index: Int, value: Long) = setLong(index, value)

    operator fun get(index: Int): Long = getLong(index)

    inline fun forEachIndexed(crossinline block: (i: Int, v: Long) -> Unit) {
        var i = 0
        val step = typeSize
        while (i < size) {
            block(i, get(i))
            i += step
        }
    }

    inline fun forEach(crossinline block: (v: Long) -> Unit) {
        forEachIndexed { _, v -> block(v) }
    }

    inline fun filterIndexed(crossinline block: (i: Int, v: Long) -> Boolean): LongList {
        var i = 0
        val step = typeSize
        val filtered = clone()
        filtered.clear()
        while (i < size) {
            val v = get(i)
            if (block(i, v)) filtered.add(v)
            i += step
        }
        return filtered
    }

    inline fun filter(crossinline block: (v: Long) -> Boolean): LongList {
        return filterIndexed { _, v -> block(v) }
    }

    inline fun findIndexed(default: Long, crossinline block: (i: Int, v: Long) -> Boolean): Long {
        var i = 0
        val step = typeSize
        while (i < size) {
            val v = get(i)
            if (block(i, v)) return v
            i += step
        }
        return default
    }

    inline fun find(default: Long, crossinline block: (v: Long) -> Boolean): Long {
        return findIndexed(default) { _, v -> block(v) }
    }
}