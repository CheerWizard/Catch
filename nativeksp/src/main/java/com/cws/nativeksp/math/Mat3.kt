package com.cws.nativeksp.math

import com.cws.nativeksp.MemoryHeap

@JvmInline
value class Mat3(
    val index: Int,
) {

    var v1: Vec3
        get() = MemoryHeap.getVec3(index + Vec3.SIZE_BYTES * 0)
        set(value) {
            MemoryHeap.setVec3(index + Vec3.SIZE_BYTES * 0, value)
        }

    var v2: Vec3
        get() = MemoryHeap.getVec3(index + Vec3.SIZE_BYTES * 1)
        set(value) {
            MemoryHeap.setVec3(index + Vec3.SIZE_BYTES * 1, value)
        }

    var v3: Vec3
        get() = MemoryHeap.getVec3(index + Vec3.SIZE_BYTES * 2)
        set(value) {
            MemoryHeap.setVec3(index + Vec3.SIZE_BYTES * 2, value)
        }

    fun free() {
        MemoryHeap.free(index, SIZE_BYTES)
    }

    companion object {
        const val SIZE_BYTES = Vec2.SIZE_BYTES * 3
        fun create(): Mat3 = Mat3(MemoryHeap.allocate(SIZE_BYTES))
    }

}

inline fun <T> Mat3.use(block: (Mat3) -> T): T {
    try {
        return block(this)
    } finally {
        free()
    }
}