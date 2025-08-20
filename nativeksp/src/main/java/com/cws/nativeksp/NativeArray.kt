package com.cws.nativeksp

class NativeArray<T>(
    size: Int,
    val elementSizeBytes: Int,
    val fromIndex: (Int) -> T,
    val toIndex: (T) -> Int,
) : Iterable<T> {

    val indices: IntArray = IntArray(size)

    val size: Int
        get() = indices.size

    operator fun set(i: Int, value: T) { indices[i] = toIndex(value) }
    operator fun get(i: Int): T = fromIndex(indices[i])
    override fun iterator(): Iterator<T> = indices.map(fromIndex).iterator()

    fun create(): NativeArray<T> {
        for (i in indices.indices) {
            if (indices[i] == MEMORY_INDEX_NULL) {
                indices[i] = MemoryHeap.allocate(elementSizeBytes)
            }
        }
        return this
    }

    fun free(): NativeArray<T> {
        for (i in indices.indices) {
            if (indices[i] != MEMORY_INDEX_NULL) {
                MemoryHeap.free(indices[i], elementSizeBytes)
                indices[i] = MEMORY_INDEX_NULL
            }
        }
        return this
    }

}