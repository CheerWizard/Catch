package com.cws.kmemory.collections

import com.cws.kmemory.FastList

class ShortList(capacity: Int) : FastList(capacity, Short.SIZE_BYTES) {

    fun clone(): ShortList = ShortList(capacity)

    fun add(value: Short) = addShort(value)

    operator fun set(index: Int, value: Short) = setShort(index, value)

    operator fun get(index: Int): Short = getShort(index)

    inline fun forEachIndexed(crossinline block: (i: Int, v: Short) -> Unit) {
        var i = 0
        val step = typeSize
        while (i < size) {
            block(i, get(i))
            i += step
        }
    }

    inline fun forEach(crossinline block: (v: Short) -> Unit) {
        forEachIndexed { _, v -> block(v) }
    }

    inline fun filterIndexed(crossinline block: (i: Int, v: Short) -> Boolean): ShortList {
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

    inline fun filter(crossinline block: (v: Short) -> Boolean): ShortList {
        return filterIndexed { _, v -> block(v) }
    }

    inline fun findIndexed(default: Short, crossinline block: (i: Int, v: Short) -> Boolean): Short {
        var i = 0
        val step = typeSize
        while (i < size) {
            val v = get(i)
            if (block(i, v)) return v
            i += step
        }
        return default
    }

    inline fun find(default: Short, crossinline block: (v: Short) -> Boolean): Short {
        return findIndexed(default) { _, v -> block(v) }
    }
}