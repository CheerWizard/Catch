package com.cws.acatch.graphics

import android.opengl.GLES30.*

class IndexBuffer(size: Int) : GLBuffer(
    type = GL_ELEMENT_ARRAY_BUFFER,
    elementSizeBytes = Int.SIZE_BYTES,
    size = size
) {

    private var bufferView = buffer.asIntBuffer()

    fun add(indices: IntArray) {
        ensureCapacity(size = indices.size)
        bufferView.put(indices)
    }

    fun update(index: Int, indices: IntArray) {
        ensureCapacity(index = index, size = indices.size)
        update(index) {
            bufferView.put(indices)
        }
    }

    override fun resize(newCapacity: Int) {
        super.resize(newCapacity)
        bufferView = buffer.asIntBuffer()
    }

}