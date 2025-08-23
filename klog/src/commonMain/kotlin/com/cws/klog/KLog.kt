package com.cws.klog

expect object KLog {
    fun verbose(message: String)
    fun info(message: String)
    fun debug(message: String)
    fun warn(message: String)
    fun error(message: String)
    fun error(message: String, exception: Throwable)
    fun tag(): String
}