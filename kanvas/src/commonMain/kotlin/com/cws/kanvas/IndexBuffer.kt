package com.cws.kanvas

import com.cws.kmemory.FastList

class IndexBuffer(size: Int) : GpuBuffer(
    type = Kanvas.INDEX_BUFFER,
    typeSize = Int.SIZE_BYTES,
    capacity = size
) {

    fun add(indices: FastList) {
        ensureCapacity(size = indices.size)
        addArray(indices)
    }

    fun update(index: Int, indices: FastList) {
        ensureCapacity(index = index, size = indices.size)
        update(index) {
            setArray(index, indices)
        }
    }

}