package com.cws.kmemory.math

import com.cws.kmemory.NativeHeap
import kotlin.jvm.JvmInline

@JvmInline
value class Mat2(
    val index: Int,
) {

    var v1: Vec2
        get() = Vec2(index + Vec2.SIZE_BYTES * 0)
        set(value) {
            NativeHeap.copy(value.index, index + Vec2.SIZE_BYTES * 0, Vec2.SIZE_BYTES)
        }

    var v2: Vec2
        get() = Vec2(index + Vec2.SIZE_BYTES * 1)
        set(value) {
            NativeHeap.copy(value.index, index + Vec2.SIZE_BYTES * 1, Vec2.SIZE_BYTES)
        }

    fun free() {
        NativeHeap.free(index, SIZE_BYTES)
    }

    companion object {
        const val SIZE_BYTES = Vec2.SIZE_BYTES * 2
        fun create(): Mat2 = Mat2(NativeHeap.allocate(SIZE_BYTES))
    }

}

inline fun <T> Mat2.use(block: (Mat2) -> T): T {
    try {
        return block(this)
    } finally {
        free()
    }
}