package com.cws.printer

expect class FileLogger {
    fun open(name: String, filepath: String)
    fun close()
    fun log(message: String)
}