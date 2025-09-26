package com.cws.fmm

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

object FastProfiler {

    private val PROFILING_PERIOD = 5.seconds

    private var job: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun start() {
        if (job?.isActive == true) return
        job = GlobalScope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(PROFILING_PERIOD)
            }
        }
    }

    fun stop() {
        if (job?.isActive == false) return
        job?.cancel()
    }

}