package com.cws.fmm

class StackMemory(
    private val handle: MemoryHandle,
    private val capacity: Int
) {

    companion object {
        const val SIZE_BYTES = 1024 * 1024
    }

    var position: Int = 0
        private set

    fun push(size: Int): MemoryHandle {
        if (position == capacity) {
            throw RuntimeException("StackMemory overflow error, size = $size bytes")
        }
        position += size
        return handle + position
    }

    fun pop(size: Int) {
        if (position < size) {
            throw RuntimeException("StackMemory underflow error, size = $size bytes")
        } else {
            position -= size
        }
    }

}

object StackMemoryManager {

    private val stacks = Array(getMaxThreadCount()) {
        StackMemory(
            handle = HeapMemory.allocate(StackMemory.SIZE_BYTES),
            capacity = StackMemory.SIZE_BYTES
        )
    }

    fun getStack(): StackMemory = stacks[getIndex()]

    private fun getIndex(): Int {
        return (getCurrentThreadID().hashCode() and Int.MAX_VALUE) % stacks.size
    }

}

inline fun stack(block: StackMemory.() -> Unit) {
    val stackMemory = StackMemoryManager.getStack()
    val begin = stackMemory.position
    block(stackMemory)
    val end = stackMemory.position
    stackMemory.pop(end - begin)
}