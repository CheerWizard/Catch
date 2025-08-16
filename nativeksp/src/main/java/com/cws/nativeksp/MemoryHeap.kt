package com.cws.nativeksp

class FreeBlocks(size: Int) {

    private val indices = ArrayList<Int>(size)
    private val sizes = ArrayList<Int>(size)
    private var position = -1

    @Synchronized
    fun push(index: Int, size: Int) {
        ++position
        indices.add(position, index)
        sizes.add(position, size)
    }

    @Synchronized
    fun pop(size: Int): Int {
        if (position < 0) return MEMORY_INDEX_NULL
        var freeSize = 0
        while (freeSize < size) {
            freeSize = sizes[position]
            if (freeSize == size) {
                return indices[position--]
            }
            else if (freeSize > size) {
                val freeIndex = indices[position]
                indices.add(position, freeIndex + size)
                sizes.add(position, freeSize - size)
                return freeIndex
            }
        }
        return MEMORY_INDEX_NULL
    }

}

object MemoryHeap : MemoryBuffer(1024 * 1024) {

    private val freeBlocks = FreeBlocks(100)

    @Synchronized
    fun allocate(size: Int): Int {
        val freeIndex = freeBlocks.pop(size)
        if (freeIndex != MEMORY_INDEX_NULL) {
            return freeIndex
        }

        val index = buffer.position()
        val capacity = buffer.capacity()
        if (index == capacity) {
            resize(capacity * 2)
        }

        buffer.position(index + size)
        return index
    }

    @Synchronized
    fun free(index: Int, size: Int) {
        if (index == MEMORY_INDEX_NULL) return
        freeBlocks.push(index, size)
    }

}