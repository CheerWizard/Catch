package com.cws.kmemory
data class MemoryInfo(
    val totalHeapSize: Long,
    val freeHeapSize: Long,
    val totalPhysicalSize: Long,
    val freePhysicalSize: Long
)

// it's a syscall, it's not cached by default
expect fun getMemoryInfo(): MemoryInfo