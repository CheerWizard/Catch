package com.cws.kmemory

data class ThreadInfo(
    val id: Int,
    val name: String,
    val maxCount: Int,
)
expect fun getCurrentThreadID(): Int
expect fun getMaxThreadCount(): Int