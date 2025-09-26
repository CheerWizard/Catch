package com.cws.fmm

data class MemoryInfo(
    val totalHeapSize: Long,
    val freeHeapSize: Long,
    val totalPhysicalSize: Long,
    val freePhysicalSize: Long
)

private lateinit var memoryInfo: MemoryInfo

fun getMemoryInfo(): MemoryInfo {
    if (!::memoryInfo.isInitialized) {
        memoryInfo = fetchMemoryInfo()
    }
    return memoryInfo
}

internal expect fun fetchMemoryInfo(): MemoryInfo