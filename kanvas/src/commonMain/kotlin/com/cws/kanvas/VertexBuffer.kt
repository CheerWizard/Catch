package com.cws.kanvas

class VertexBuffer(size: Int) : GpuBuffer(
    type = Kanvas.VERTEX_BUFFER,
    elementSizeBytes = Float.SIZE_BYTES,
    size = size
) {

    fun add(vertices: Vertices) {
        ensureCapacity(size = vertices.values.size)
        setArray(vertices.values)
    }

    fun update(index: Int, vertices: Vertices) {
        ensureCapacity(index = index, size = vertices.values.size)
        update(index) {
            setArray(vertices.values)
        }
    }

}