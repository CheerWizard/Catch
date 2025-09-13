package com.cws.kmemory.collections

import com.cws.kmemory.FastList

class IntList(capacity: Int) : FastList(capacity, Int.SIZE_BYTES) {

    fun clone(): IntList = IntList(capacity)

    fun add(value: Int) = addInt(value)

    operator fun set(index: Int, value: Int) = setInt(index, value)

    operator fun get(index: Int): Int = getInt(index)

    inline fun forEachIndexed(crossinline block: (i: Int, v: Int) -> Unit) {
        var i = 0
        val step = typeSize
        while (i < size) {
            block(i, get(i))
            i += step
        }
    }

    inline fun forEach(crossinline block: (v: Int) -> Unit) {
        forEachIndexed { _, v -> block(v) }
    }

    inline fun filterIndexed(crossinline block: (i: Int, v: Int) -> Boolean): IntList {
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

    inline fun filter(crossinline block: (v: Int) -> Boolean): IntList {
        return filterIndexed { _, v -> block(v) }
    }

    inline fun findIndexed(default: Int, crossinline block: (i: Int, v: Int) -> Boolean): Int {
        var i = 0
        val step = typeSize
        while (i < size) {
            val v = get(i)
            if (block(i, v)) return v
            i += step
        }
        return default
    }

    inline fun find(default: Int, crossinline block: (v: Int) -> Boolean): Int {
        return findIndexed(default) { _, v -> block(v) }
    }
}