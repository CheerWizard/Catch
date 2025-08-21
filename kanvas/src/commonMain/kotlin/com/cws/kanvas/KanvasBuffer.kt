package com.cws.kanvas

import com.cws.kmemory.NativeBuffer

open class KanvasBuffer(
    protected val type: Int,
    protected val elementSizeBytes: Int,
    size: Int
) : NativeBuffer(size) {

    protected lateinit var handle: BufferID

    open fun init() {
        handle = Kanvas.bufferInit()
        resizeBuffer()
    }

    open fun release() {
        Kanvas.bufferRelease(handle)
    }

    open fun bind() {
        Kanvas.bufferBind(type, handle)
    }

    fun flush(index: Int = 0, size: Int = position) {
        bind()
        setPosition(index * elementSizeBytes)
        Kanvas.bufferSubData(
            type = type,
            size = size * elementSizeBytes,
            offset = index * elementSizeBytes,
            data = getBuffer()
        )
    }

    override fun resize(newCapacity: Int) {
        super.resize(newCapacity)
        resizeBuffer()
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

    protected fun resizeBuffer() {
        bind()
        Kanvas.bufferData(
            type = type,
            data = null,
            offset = 0,
            size = capacity * elementSizeBytes,
            usage = Kanvas.DYNAMIC_DRAW
        )
    }

}