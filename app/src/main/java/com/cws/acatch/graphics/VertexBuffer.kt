package com.cws.acatch.graphics

import android.opengl.GLES30.GL_ARRAY_BUFFER

class VertexBuffer(size: Int) : GLBuffer(
    type = GL_ARRAY_BUFFER,
    elementSizeBytes = Float.SIZE_BYTES,
    size = size
) {

    private var bufferView = buffer.asFloatBuffer()

    fun add(vertices: Vertices) {
        ensureCapacity(size = vertices.values.size)
        bufferView.put(vertices.values)
    }

    fun update(index: Int, vertices: Vertices) {
        ensureCapacity(index = index, size = vertices.values.size)
        update(index) {
            bufferView.put(vertices.values)
        }
    }

    override fun resize(newCapacity: Int) {
        super.resize(newCapacity)
        bufferView = buffer.asFloatBuffer()
    }

}