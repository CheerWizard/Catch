package com.cws.acatch.graphics

import android.opengl.GLES30.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

open class GLBuffer(
    protected val type: Int,
    protected val elementSizeBytes: Int,
    size: Int
) {

    protected val handle = IntArray(1)
    protected var buffer: ByteBuffer = createBuffer(size * elementSizeBytes)

    open fun init() {
        glGenBuffers(1, handle, 0)
        resizeGPUBuffer()
    }

    open fun release() {
        glDeleteBuffers(1, handle, 0)
    }

    open fun bind() {
        glBindBuffer(type, handle[0])
    }

    protected fun ensureCapacity(index: Int = buffer.position(), size: Int) {
        if (index + size > buffer.capacity()) {
            resize(buffer.capacity() * 2)
        }
    }

    protected inline fun update(index: Int, updateBlock: () -> Unit) {
        val lastIndex = buffer.position()
        buffer.position(index)
        updateBlock()
        buffer.position(lastIndex)
    }

    fun flush(index: Int = 0, size: Int = buffer.position()) {
        bind()
        buffer.position(index * elementSizeBytes)
        glBufferSubData(type, index * elementSizeBytes, size * elementSizeBytes, buffer)
    }

    protected open fun resize(newCapacity: Int) {
        val newBuffer = createBuffer(newCapacity)
        buffer.flip()
        newBuffer.put(buffer)
        buffer = newBuffer
        resizeGPUBuffer()
    }

    protected fun resizeGPUBuffer() {
        bind()
        glBufferData(type, buffer.capacity() * elementSizeBytes, null, GL_DYNAMIC_DRAW)
    }

    private fun createBuffer(size: Int): ByteBuffer {
        return ByteBuffer
            .allocateDirect(size)
            .order(ByteOrder.nativeOrder())
    }

}