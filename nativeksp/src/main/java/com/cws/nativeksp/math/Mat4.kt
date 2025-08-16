package com.cws.nativeksp.math

import com.cws.nativeksp.MemoryHeap

@JvmInline
value class Mat4(
    val index: Int,
) {

    var v1: Vec4
        get() = MemoryHeap.getVec4(index + Vec4.SIZE_BYTES * 0)
        set(value) {
            MemoryHeap.setVec4(index + Vec4.SIZE_BYTES * 0, value)
        }

    var v2: Vec4
        get() = MemoryHeap.getVec4(index + Vec4.SIZE_BYTES * 1)
        set(value) {
            MemoryHeap.setVec4(index + Vec4.SIZE_BYTES * 1, value)
        }

    var v3: Vec4
        get() = MemoryHeap.getVec4(index + Vec4.SIZE_BYTES * 2)
        set(value) {
            MemoryHeap.setVec4(index + Vec4.SIZE_BYTES * 2, value)
        }

    var v4: Vec4
        get() = MemoryHeap.getVec4(index + Vec4.SIZE_BYTES * 3)
        set(value) {
            MemoryHeap.setVec4(index + Vec4.SIZE_BYTES * 3, value)
        }

    fun free() {
        MemoryHeap.free(index, SIZE_BYTES)
    }

    companion object {
        const val SIZE_BYTES = Vec2.SIZE_BYTES * 4
        fun create(): Mat4 = Mat4(MemoryHeap.allocate(SIZE_BYTES))
    }

}

inline fun <T> Mat4.use(block: (Mat4) -> T): T {
    try {
        return block(this)
    } finally {
        free()
    }
}