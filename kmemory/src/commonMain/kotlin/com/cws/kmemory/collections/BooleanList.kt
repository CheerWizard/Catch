package com.cws.kmemory.collections

import com.cws.kmemory.FastList

class BooleanList(capacity: Int) : FastList(capacity, Byte.SIZE_BYTES) {

    fun clone(): BooleanList = BooleanList(capacity)

    fun add(value: Boolean) = addBoolean(value)

    operator fun set(index: Int, value: Boolean) = setBoolean(index, value)

    operator fun get(index: Int): Boolean = getBoolean(index)

    inline fun forEachIndexed(crossinline block: (i: Int, v: Boolean) -> Unit) {
        var i = 0
        val step = typeSize
        while (i < size) {
            block(i, get(i))
            i += step
        }
    }

    inline fun forEach(crossinline block: (v: Boolean) -> Unit) {
        forEachIndexed { _, v -> block(v) }
    }

    inline fun filterIndexed(crossinline block: (i: Int, v: Boolean) -> Boolean): BooleanList {
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

    inline fun filter(crossinline block: (v: Boolean) -> Boolean): BooleanList {
        return filterIndexed { _, v -> block(v) }
    }

    inline fun findIndexed(default: Boolean, crossinline block: (i: Int, v: Boolean) -> Boolean): Boolean {
        var i = 0
        val step = typeSize
        while (i < size) {
            val v = get(i)
            if (block(i, v)) return v
            i += step
        }
        return default
    }

    inline fun find(default: Boolean, crossinline block: (v: Boolean) -> Boolean): Boolean {
        return findIndexed(default) { _, v -> block(v) }
    }
}