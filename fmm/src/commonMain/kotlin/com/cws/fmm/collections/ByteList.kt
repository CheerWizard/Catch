package com.cws.fmm.collections

import com.cws.fmm.FastList

class ByteList(capacity: Int) : FastList(capacity, Byte.SIZE_BYTES) {

    fun clone(): ByteList = ByteList(capacity)

    fun add(value: Byte) = addByte(value)

    operator fun set(index: Int, value: Byte) = setByte(index, value)

    operator fun get(index: Int): Byte = getByte(index)

    inline fun forEachIndexed(crossinline block: (i: Int, v: Byte) -> Unit) {
        var i = 0
        val step = typeSize
        while (i < size) {
            block(i, get(i))
            i += step
        }
    }

    inline fun forEach(crossinline block: (v: Byte) -> Unit) {
        forEachIndexed { _, v -> block(v) }
    }

    inline fun filterIndexed(crossinline block: (i: Int, v: Byte) -> Boolean): ByteList {
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

    inline fun filter(crossinline block: (v: Byte) -> Boolean): ByteList {
        return filterIndexed { _, v -> block(v) }
    }

    inline fun findIndexed(default: Byte, crossinline block: (i: Int, v: Byte) -> Boolean): Byte {
        var i = 0
        val step = typeSize
        while (i < size) {
            val v = get(i)
            if (block(i, v)) return v
            i += step
        }
        return default
    }

    inline fun find(default: Byte, crossinline block: (v: Byte) -> Boolean): Byte {
        return findIndexed(default) { _, v -> block(v) }
    }
}