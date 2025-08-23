package com.cws.kanvas

import com.cws.kmemory.NativeArray
import com.cws.kmemory.NativeHeap

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
        copy(
            srcBuffer = NativeHeap,
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