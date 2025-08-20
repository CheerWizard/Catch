package com.cws.acatch.graphics

import android.opengl.GLES30.*
import com.cws.nativeksp.NativeArray
import com.cws.nativeksp.MemoryHeap

open class UniformBuffer<T>(
    private val binding: Int,
    structSize: Int,
    structCount: Int
) : GLBuffer(
    type = GL_UNIFORM_BUFFER,
    elementSizeBytes = structSize,
    size = structCount,
) {

    override fun init() {
        super.init()
        glBindBufferBase(GL_UNIFORM_BUFFER, binding, handle[0])
    }

    fun update(index: Int, heapIndex: Int) {
        MemoryHeap.copy(
            destBuffer = buffer,
            src = heapIndex,
            dest = index * elementSizeBytes,
            size = elementSizeBytes
        )
    }

    fun update(nativeArray: NativeArray<T>) {
        nativeArray.forEachIndexed { i, data ->
            update(i, nativeArray.toIndex(data))
        }
    }

}