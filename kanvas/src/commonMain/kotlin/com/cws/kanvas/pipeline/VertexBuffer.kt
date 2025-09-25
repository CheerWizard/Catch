package com.cws.kanvas.pipeline

import com.cws.kanvas.core.Kanvas
import com.cws.kmemory.FastList

class VertexBuffer(size: Int) : GpuBuffer(
    type = Kanvas.VERTEX_BUFFER,
    typeSize = Vertex.SIZE_BYTES,
    capacity = size
) {

    fun add(vertices: FastList) {
        ensureCapacity(size = vertices.size)
        addArray(vertices)
    }

    fun update(index: Int, vertices: FastList) {
        ensureCapacity(index = index, size = vertices.size)
        update(index) {
            setArray(index, vertices)
        }
    }

}