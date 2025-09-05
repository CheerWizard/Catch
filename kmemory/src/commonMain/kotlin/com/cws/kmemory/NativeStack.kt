package com.cws.kmemory

class NativeStack(
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

object NativeStackMap {

    private val stacks: HashMap<Int, NativeStack> = HashMap(getMaxThreadCount())

    fun getStack() = stacks.getOrPut(getCurrentThreadID()) {
        NativeStack(
            NativeHeap.allocate(NativeStack.SIZE_BYTES),
            NativeStack.SIZE_BYTES
        )
    }

}

inline fun stack(block: NativeStack.() -> Unit) {
    val nativeStack = NativeStackMap.getStack()
    val begin = nativeStack.position
    block(nativeStack)
    val end = nativeStack.position
    nativeStack.pop(end - begin)
}