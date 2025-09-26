package com.cws.fmm

typealias MemoryHandle = Int
typealias MemoryHandleArray = IntArray

const val NULL: MemoryHandle = -1

inline val MemoryHandle.isNull get() = this == NULL

fun MemoryHandle.checkNotNull() {
    if (isNull) {
        throw NullPointerException("MemoryHandle is NULL!")
    }
}