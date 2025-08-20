package com.cws.kmemory.math

import com.cws.kmemory.NativeHeap
import kotlin.jvm.JvmInline

@JvmInline
value class Mat3(
    val index: Int,
) {

    var v1: Vec3
        get() = NativeHeap.getVec3(index + Vec3.SIZE_BYTES * 0)
        set(value) {
            NativeHeap.setVec3(index + Vec3.SIZE_BYTES * 0, value)
        }

    var v2: Vec3
        get() = NativeHeap.getVec3(index + Vec3.SIZE_BYTES * 1)
        set(value) {
            NativeHeap.setVec3(index + Vec3.SIZE_BYTES * 1, value)
        }

    var v3: Vec3
        get() = NativeHeap.getVec3(index + Vec3.SIZE_BYTES * 2)
        set(value) {
            NativeHeap.setVec3(index + Vec3.SIZE_BYTES * 2, value)
        }

    fun free() {
        NativeHeap.free(index, SIZE_BYTES)
    }

    companion object {
        const val SIZE_BYTES = Vec3.SIZE_BYTES * 3
        fun create(): Mat3 = Mat3(NativeHeap.allocate(SIZE_BYTES))
    }

}

inline fun <T> Mat3.use(block: (Mat3) -> T): T {
    try {
        return block(this)
    } finally {
        free()
    }
}