package com.cws.acatch.graphics

import android.opengl.GLES30.*
import com.cws.kmemory.NativeBuffer
import java.nio.ByteBuffer

open class GLBuffer(
    protected val type: Int,
    protected val elementSizeBytes: Int,
    size: Int
) : NativeBuffer(size * elementSizeBytes) {

    protected val handle = IntArray(1)

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

    protected fun ensureCapacity(index: Int = position, size: Int) {
        if (index + size > capacity) {
            resize(capacity * 2)
        }
    }

    protected inline fun update(index: Int, updateBlock: () -> Unit) {
        val lastIndex = position
        setPosition(index)
        updateBlock()
        setPosition(lastIndex)
    }

    fun flush(index: Int = 0, size: Int = position) {
        bind()
        setPosition(index * elementSizeBytes)
        glBufferSubData(type, index * elementSizeBytes, size * elementSizeBytes, getBuffer() as ByteBuffer)
    }

    override fun resize(newCapacity: Int) {
        super.resize(newCapacity)
        resizeGPUBuffer()
    }

    protected fun resizeGPUBuffer() {
        bind()
        glBufferData(type, capacity * elementSizeBytes, null, GL_DYNAMIC_DRAW)
    }

}