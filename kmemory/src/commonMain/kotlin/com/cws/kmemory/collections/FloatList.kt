package com.cws.kmemory.collections

import com.cws.kmemory.FastList

class FloatList(capacity: Int) : FastList(capacity, Float.SIZE_BYTES) {

    fun clone(): FloatList = FloatList(capacity)

    fun add(value: Float) = addFloat(value)

    operator fun set(index: Int, value: Float) = setFloat(index, value)

    operator fun get(index: Int): Float = getFloat(index)

    inline fun forEachIndexed(crossinline block: (i: Int, v: Float) -> Unit) {
        var i = 0
        val step = typeSize
        while (i < size) {
            block(i, get(i))
            i += step
        }
    }

    inline fun forEach(crossinline block: (v: Float) -> Unit) {
        forEachIndexed { _, v -> block(v) }
    }

    inline fun filterIndexed(crossinline block: (i: Int, v: Float) -> Boolean): FloatList {
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

    inline fun filter(crossinline block: (v: Float) -> Boolean): FloatList {
        return filterIndexed { _, v -> block(v) }
    }

    inline fun findIndexed(default: Float, crossinline block: (i: Int, v: Float) -> Boolean): Float {
        var i = 0
        val step = typeSize
        while (i < size) {
            val v = get(i)
            if (block(i, v)) return v
            i += step
        }
        return default
    }

    inline fun find(default: Float, crossinline block: (v: Float) -> Boolean): Float {
        return findIndexed(default) { _, v -> block(v) }
    }
}