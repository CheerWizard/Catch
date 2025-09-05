package com.cws.kmemory.math

import com.cws.kmemory.NativeHeap
import com.cws.kmemory.clone
import kotlin.jvm.JvmInline

@JvmInline
value class Mat4(
    val index: Int,
) {

    var v1: Vec4
        get() = Vec4(index + Vec4.SIZE_BYTES * 0)
        set(value) {
            NativeHeap.clone(value.index, index + Vec4.SIZE_BYTES * 0, Vec4.SIZE_BYTES)
        }

    var v2: Vec4
        get() = Vec4(index + Vec4.SIZE_BYTES * 1)
        set(value) {
            NativeHeap.clone(value.index, index + Vec4.SIZE_BYTES * 1, Vec4.SIZE_BYTES)
        }

    var v3: Vec4
        get() = Vec4(index + Vec4.SIZE_BYTES * 2)
        set(value) {
            NativeHeap.clone(value.index, index + Vec4.SIZE_BYTES * 2, Vec4.SIZE_BYTES)
        }

    var v4: Vec4
        get() = Vec4(index + Vec4.SIZE_BYTES * 3)
        set(value) {
            NativeHeap.clone(value.index, index + Vec4.SIZE_BYTES * 3, Vec4.SIZE_BYTES)
        }

    fun free() {
        NativeHeap.free(index, SIZE_BYTES)
    }

    companion object {
        const val SIZE_BYTES = Vec4.SIZE_BYTES * 4
        fun create(): Mat4 = Mat4(NativeHeap.allocate(SIZE_BYTES))
    }

}

inline fun <T> Mat4.use(block: (Mat4) -> T): T {
    try {
        return block(this)
    } finally {
        free()
    }
}