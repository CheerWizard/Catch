package com.cws.kmemory

class FreeBlocks(size: Int) {

    private val indices = SmallArray<Int>(size)
    private val sizes = SmallArray<Int>(size)
    private var position = -1

    fun push(index: Int, size: Int) {
        indices[++position] = index
        sizes[position] = size
    }

    fun pop(size: Int): Int {
        if (position < 0) return MEMORY_INDEX_NULL
        var freeSize = 0
        while (freeSize < size) {
            freeSize = sizes[position]
            if (freeSize == size) {
                return indices[position--]
            } else if (freeSize > size) {
                val freeIndex: Int = indices[position]
                indices[position] = freeIndex + size
                sizes[position] = freeSize - size
                return freeIndex
            }
        }
        return MEMORY_INDEX_NULL
    }

}

object NativeHeap : NativeArray(BigBuffer(1024 * 1024 * 256)) {

    private val freeBlocks = FreeBlocks(100)

    fun allocate(size: Int): Int = lock {
        val freeIndex = freeBlocks.pop(size)
        if (freeIndex != MEMORY_INDEX_NULL) {
            return@lock freeIndex
        }

        val index = position
        val capacity = capacity
        if (index == capacity) {
            resize(capacity * 2)
        }

        position = index + size
        return@lock index
    }

    fun free(index: Int, size: Int) {
        if (index == MEMORY_INDEX_NULL) return
        lock {
            freeBlocks.push(index, size)
        }
    }

}