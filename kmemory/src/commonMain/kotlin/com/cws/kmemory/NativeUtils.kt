package com.cws.kmemory

inline val <reified T> T.bits get(): Long {
    return when (T::class) {
        Byte::class -> (this as Byte).toLong()
        Boolean::class -> if (this as Boolean) 1L else 0L
        Short::class -> (this as Short).toLong()
        Int::class -> (this as Int).toLong()
        Long::class -> this as Long
        Float::class -> (this as Float).toBits().toLong()
        Double::class -> (this as Double).toBits()
        else -> error("Unsupported type!")
    }
}

inline val <reified T> T.bytes get(): Int = sizeof<T>()

inline fun <reified T> sizeof(): Int {
    return when (T::class) {
        Byte::class -> Byte.SIZE_BYTES
        Boolean::class -> Byte.SIZE_BYTES
        Short::class -> Short.SIZE_BYTES
        Int::class -> Int.SIZE_BYTES
        Long::class -> Long.SIZE_BYTES
        Float::class -> Float.SIZE_BYTES
        Double::class -> Double.SIZE_BYTES
        else -> error("Unsupported type!")
    }
}