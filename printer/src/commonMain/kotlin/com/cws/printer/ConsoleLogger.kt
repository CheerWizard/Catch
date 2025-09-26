package com.cws.printer

expect object ConsoleLogger {
    fun v(message: String = "")
    fun i(message: String = "")
    fun d(message: String = "")
    fun w(message: String = "")
    fun e(message: String = "")
    fun e(message: String = "", exception: Throwable)
    fun tag(): Tag
}