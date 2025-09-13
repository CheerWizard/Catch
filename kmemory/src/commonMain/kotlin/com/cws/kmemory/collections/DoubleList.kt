package com.cws.kmemory.collections

import com.cws.kmemory.FastList

class DoubleList(capacity: Int) : FastList(capacity, Double.SIZE_BYTES) {

    fun clone(): DoubleList = DoubleList(capacity)

    fun add(value: Double) = addDouble(value)

    operator fun set(index: Int, value: Double) = setDouble(index, value)

    operator fun get(index: Int): Double = getDouble(index)

    inline fun forEachIndexed(crossinline block: (i: Int, v: Double) -> Unit) {
        var i = 0
        val step = typeSize
        while (i < size) {
            block(i, get(i))
            i += step
        }
    }

    inline fun forEach(crossinline block: (v: Double) -> Unit) {
        forEachIndexed { _, v -> block(v) }
    }

    inline fun filterIndexed(crossinline block: (i: Int, v: Double) -> Boolean): DoubleList {
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

    inline fun filter(crossinline block: (v: Double) -> Boolean): DoubleList {
        return filterIndexed { _, v -> block(v) }
    }

    inline fun findIndexed(default: Double, crossinline block: (i: Int, v: Double) -> Boolean): Double {
        var i = 0
        val step = typeSize
        while (i < size) {
            val v = get(i)
            if (block(i, v)) return v
            i += step
        }
        return default
    }

    inline fun find(default: Double, crossinline block: (v: Double) -> Boolean): Double {
        return findIndexed(default) { _, v -> block(v) }
    }
}