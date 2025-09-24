package com.cws.kanvas.core

import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.coroutines.Runnable

object RenderLoopJobs {

    private val jobs = ArrayDeque<Runnable>()
    private val lock = ReentrantLock()

    fun push(runnable: Runnable) {
        lock.withLock {
            jobs.addLast(runnable)
        }
    }

    fun pop(): Runnable {
        lock.withLock {
            return jobs.removeFirst()
        }
    }

    fun execute() {
        lock.withLock {
            while (jobs.isNotEmpty()) {
                jobs.removeFirst().run()
            }
        }
    }

}