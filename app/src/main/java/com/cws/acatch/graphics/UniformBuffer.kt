package com.cws.acatch.graphics

import android.opengl.GLES30.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

class UniformBuffer<T>(
    private val binding: Int,
    private val struct: T
) {

    private val handle = ByteBuffer
        .allocateDirect(4)
        .order(ByteOrder.nativeOrder())
        .asIntBuffer()

    fun init() {
        glGenBuffers(1, handle)
        bind()
//        glBufferData(GL_UNIFORM_BUFFER, bytebuffer.capacity(), bytebuffer, GL_DYNAMIC_DRAW)
        glBindBufferBase(GL_UNIFORM_BUFFER, binding, handle.get())
    }

    fun release() {
        glDeleteBuffers(1, handle)
    }

    fun bind() {
        glBindBuffer(GL_UNIFORM_BUFFER, handle.get())
    }

    fun update() {
//        val bytebuffer = struct.toByteBuffer()
//        glBufferSubData(
//            GL_UNIFORM_BUFFER,
//            0,
//            bytebuffer.capacity(),
//            bytebuffer
//        )
    }

}