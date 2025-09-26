package com.cws.fmm

actual open class NativeMemory {

    actual fun init() {
        System.loadLibrary("cmemory")
    }

}