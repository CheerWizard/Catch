package com.cws.kmemory

class FreeBlocks(size: Int) {

    private val indices = IntArray(size)
    private val sizes = IntArray(size)
    private var position = -1

    fun push(index: Int, size: Int) {
        position++
        indices[position] = index
        sizes[position] = size
    }

    fun pop(size: Int): Int {
        if (position < 0) return NULL
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
        return NULL
    }

}

object HeapMemory : FastList(
    capacity = (getMemoryInfo().totalPhysicalSize * 0.20f).toLong(),
    requireBigBuffer = true
) {

    private val freeBlocks = FreeBlocks(100)
    private val lockFree = LockFree()

    val totalSize get() = capacity
    val freeSize get() = totalSize - usedSize
    var usedSize: Int = 0
        private set
    var allocations: Int = 0
        private set

    fun allocate(size: Int): Int = lockFree.lock {
        val freeIndex = freeBlocks.pop(size)
        if (freeIndex != NULL) {
            return@lock freeIndex
        }

        val index = position
        val capacity = capacity
        if (index == capacity) {
            resize(capacity * 2)
        }

        allocations++
        usedSize += size
        position = index + size
        index
    }

    fun free(index: Int, size: Int) {
        if (index == NULL) return
        lockFree.lock {
            freeBlocks.push(index, size)
            usedSize -= size
        }
    }

}