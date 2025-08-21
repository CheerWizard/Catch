package com.cws.kanvas

class IndexBuffer(size: Int) : KanvasBuffer(
    type = Kanvas.INDEX_BUFFER,
    elementSizeBytes = Int.SIZE_BYTES,
    size = size
) {

    fun add(indices: IntArray) {
        ensureCapacity(size = indices.size)
        setArray(indices)
    }

    fun update(index: Int, indices: IntArray) {
        ensureCapacity(index = index, size = indices.size)
        update(index) {
            setArray(indices)
        }
    }

}