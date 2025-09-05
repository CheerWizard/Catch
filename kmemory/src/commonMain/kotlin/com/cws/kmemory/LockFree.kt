package com.cws.kmemory

import kotlinx.atomicfu.atomic

open class LockFree {

    protected val lock = atomic(false)

    protected inline fun <T> lock(block: () -> T): T {
        while (!lock.compareAndSet(expect = false, update = true)) {}
        val result = block()
        lock.value = false
        return result
    }

}