package com.cws.kmemory

actual open class NativeMemory {

    actual fun init() {
        System.loadLibrary("cmemory")
    }

}