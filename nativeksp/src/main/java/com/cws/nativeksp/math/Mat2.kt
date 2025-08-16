package com.cws.nativeksp.math

import com.cws.nativeksp.MemoryHeap

@JvmInline
value class Mat2(
    val index: Int,
) {

    var v1: Vec2
        get() = MemoryHeap.getVec2(index + Vec2.SIZE_BYTES * 0)
        set(value) {
            MemoryHeap.setVec2(index + Vec2.SIZE_BYTES * 0, value)
        }

    var v2: Vec2
        get() = MemoryHeap.getVec2(index + Vec2.SIZE_BYTES * 1)
        set(value) {
            MemoryHeap.setVec2(index + Vec2.SIZE_BYTES * 1, value)
        }

    fun free() {
        MemoryHeap.free(index, SIZE_BYTES)
    }

    companion object {
        const val SIZE_BYTES = Vec2.SIZE_BYTES * 2
        fun create(): Mat2 = Mat2(MemoryHeap.allocate(SIZE_BYTES))
    }

}

inline fun <T> Mat2.use(block: (Mat2) -> T): T {
    try {
        return block(this)
    } finally {
        free()
    }
}