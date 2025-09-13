package com.cws.kmemory

import kotlinx.atomicfu.atomic

open class LockFree {

    val _lock = atomic(false)

    inline fun <reified T : Any> lock(block: () -> T): T {
        while (!_lock.compareAndSet(expect = false, update = true)) {}
        val result = block()
        _lock.value = false
        return result
    }

}