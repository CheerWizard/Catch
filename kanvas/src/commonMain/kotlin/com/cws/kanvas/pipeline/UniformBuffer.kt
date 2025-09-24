package com.cws.kanvas.pipeline

import com.cws.kanvas.core.Kanvas
import com.cws.kmemory.HeapMemory

open class UniformBuffer(
    private val binding: Int,
    typeSize: Int,
    count: Int
) : GpuBuffer(
    type = Kanvas.UNIFORM_BUFFER,
    typeSize = typeSize,
    capacity = count * typeSize,
) {

    override fun init() {
        super.init()
        Kanvas.bufferBindLocation(type, handle, binding)
    }

    fun update(index: Int, heapIndex: Int) {
        HeapMemory.copyTo(this, heapIndex, index * typeSize, typeSize)
    }

}