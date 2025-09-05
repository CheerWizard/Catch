package com.cws.kmemory

import java.nio.ByteBuffer

expect open class NativeMemory() {
    protected fun init()
}

object Memory : NativeMemory() {

    init {
        init()
    }

    external fun nativeAlloc(size: Int): ByteBuffer?
    external fun nativeFree(buffer: ByteBuffer)
    external fun nativeRealloc(buffer: ByteBuffer, size: Int): ByteBuffer?

}