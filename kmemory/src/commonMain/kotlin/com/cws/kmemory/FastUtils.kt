package com.cws.kmemory

const val NULL = -1

inline val Int.isNull get() = this == NULL

fun Int.checkNotNull() {
    if (isNull) {
        throw NullPointerException("Native memory index is NULL!")
    }
}