package com.cws.klog

data class Tag(
    val className: String,
    val methodName: String
)

expect object KLog {
    var enabled: Boolean
    fun verbose(message: String = "")
    fun info(message: String = "")
    fun debug(message: String = "")
    fun warn(message: String = "")
    fun error(message: String = "")
    fun error(message: String = "", exception: Throwable)
    fun tag(): Tag
}