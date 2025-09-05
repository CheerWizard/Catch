package com.cws.kanvas

import com.cws.kmemory.NativeHeap
import com.cws.kmemory.copyFrom

open class UniformBuffer<T>(
    private val binding: Int,
    structSize: Int,
    structCount: Int
) : GpuBuffer(
    type = Kanvas.UNIFORM_BUFFER,
    elementSizeBytes = structSize,
    size = structCount,
) {

    override fun init() {
        super.init()
        Kanvas.bufferBindLocation(type, handle, binding)
    }

    fun update(index: Int, heapIndex: Int) {
        copyFrom(
            src = NativeHeap,
            srcIndex = heapIndex,
            destIndex = index * elementSizeBytes,
            size = elementSizeBytes
        )
    }

    fun update(nativeArray: NativeArray<T>) {
        nativeArray.forEachIndexed { i, data ->
            update(i, nativeArray.toIndex(data))
        }
    }

}