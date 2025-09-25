package com.cws.kmemory

import com.sun.management.OperatingSystemMXBean
import java.lang.management.ManagementFactory

actual fun getMemoryInfo(): MemoryInfo {
    val runtime = Runtime.getRuntime()
    val osBean = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean
    return MemoryInfo(
        totalHeapSize = runtime.totalMemory(),
        freeHeapSize = runtime.freeMemory(),
        totalPhysicalSize = osBean.totalMemorySize,
        freePhysicalSize = osBean.freeMemorySize
    )
}