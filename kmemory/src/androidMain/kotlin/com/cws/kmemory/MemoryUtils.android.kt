package com.cws.kmemory

import android.app.ActivityManager
import android.content.Context

actual fun getMemoryInfo(): MemoryInfo {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    val physicalMemoryInfo = ActivityManager.MemoryInfo()
    activityManager.getMemoryInfo(physicalMemoryInfo)

    val runtime = Runtime.getRuntime()

    return MemoryInfo(
        freeHeapSize = runtime.freeMemory(),
        totalHeapSize = runtime.totalMemory(),
        freePhysicalSize = physicalMemoryInfo.availMem,
        totalPhysicalSize = physicalMemoryInfo.totalMem
    )
}