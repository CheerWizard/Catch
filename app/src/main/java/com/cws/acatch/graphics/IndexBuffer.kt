package com.cws.acatch.graphics

import android.opengl.GLES30.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

class IndexBuffer(size: Int) {

    val indices = ByteBuffer
        .allocateDirect(Int.SIZE_BYTES * size)
        .order(ByteOrder.nativeOrder())

    private val handle = ByteBuffer
        .allocateDirect(4)
        .order(ByteOrder.nativeOrder())
        .asIntBuffer()

    fun init() {
        glGenBuffers(1, handle)
        bind()
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.capacity(), indices, GL_DYNAMIC_DRAW)
    }

    fun release() {
        glDeleteBuffers(1, handle)
    }

    fun bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, handle.get())
    }

    fun update() {
        glBufferSubData(GL_ARRAY_BUFFER, 0, indices.capacity(), indices)
    }

}