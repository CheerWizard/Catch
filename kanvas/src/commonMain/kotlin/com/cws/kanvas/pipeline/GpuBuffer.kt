package com.cws.kanvas.pipeline

import com.cws.kanvas.core.BufferID
import com.cws.kanvas.core.Kanvas
import com.cws.fmm.BigBuffer
import com.cws.fmm.FastBuffer
import com.cws.fmm.FastList

open class GpuBuffer(
    protected val type: Int,
    typeSize: Int,
    capacity: Int
) : FastList(
    capacity = capacity,
    typeSize = typeSize,
    requireBigBuffer = true
) {

    protected lateinit var handle: BufferID

    open fun init() {
        handle = Kanvas.bufferInit()
        resizeBuffer()
    }

    override fun release() {
        super.release()
        Kanvas.bufferRelease(handle)
    }

    open fun bind() {
        Kanvas.bufferBind(type, handle)
    }

    fun flush(index: Int = 0, size: Int = position) {
        bind()
        position = index * typeSize
        Kanvas.bufferSubData(
            type = type,
            size = size * typeSize,
            offset = position,
            data = buffer as BigBuffer
        )
    }

    override fun resize(newCapacity: Int): FastBuffer {
        super.resize(newCapacity)
        resizeBuffer()
        return buffer
    }

    protected fun ensureCapacity(index: Int = position, size: Int) {
        if (index + size > capacity) {
            resize(capacity * 2)
        }
    }

    protected inline fun update(index: Int, updateBlock: () -> Unit) {
        val lastIndex = position
        position = index
        updateBlock()
        position = lastIndex
    }

    private fun resizeBuffer() {
        bind()
        Kanvas.bufferData(
            type = type,
            data = buffer as BigBuffer,
            offset = 0,
            size = capacity,
            usage = Kanvas.DYNAMIC_DRAW
        )
    }

}