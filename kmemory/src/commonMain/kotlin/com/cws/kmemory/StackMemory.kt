package com.cws.kmemory

class StackMemory(
    index: Int,
    private val capacity: Int
) {

    companion object {
        const val SIZE_BYTES = 1024 * 1024
    }

    var position: Int = index
        private set

    fun push(size: Int): Int {
        if (position == capacity) {
            throw RuntimeException("NativeStack overflow error, size = $size bytes")
        }
        position += size
        return position
    }

    fun pop(size: Int) {
        if (position < size) {
            throw RuntimeException("NativeStack underflow error, size = $size bytes")
        } else {
            position -= size
        }
    }

}

object StackMemoryMap {

    private val stacks: HashMap<Int, StackMemory> = HashMap(getMaxThreadCount())

    fun getStack() = stacks.getOrPut(getCurrentThreadID()) {
        StackMemory(
            HeapMemory.allocate(StackMemory.SIZE_BYTES),
            StackMemory.SIZE_BYTES
        )
    }

}

inline fun stack(block: StackMemory.() -> Unit) {
    val stackMemory = StackMemoryMap.getStack()
    val begin = stackMemory.position
    block(stackMemory)
    val end = stackMemory.position
    stackMemory.pop(end - begin)
}